package com.example.shopping.services.impl;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.dto.LlmItemDTO;
import com.example.shopping.dto.PlanDTO;
import com.example.shopping.exception.EntityNotActionedException;
import com.example.shopping.exception.EntityNotFoundException;
import com.example.shopping.exception.IllegalEntityException;
import com.example.shopping.repository.ListRepository;
import com.example.shopping.services.IPlanService;
import com.google.genai.Client;
import com.google.genai.errors.ServerException;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Service
public class PlanService implements IPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanService.class);

    private final ModelMapper MODEL_MAPPER;

    private final JsonMapper JSON_MAPPER;

    private final ListRepository LIST_REPOSITORY;

    private final ChatModel GEMINI_CHAT_MODEL;

    private static final String PROMPT = """
            You are a shopping assistant. The user will provide a shopping list and may include a zip code.
            
            The shopping list will be provided as a JSON array of objects in this exact format:
            [
              { "name": "Bananas", "quantity": 5 }
            ]
            Use only the "name" and "quantity" fields.
            
            Compare only these stores, in this order: Walmart, Target.
            Attempt to look up the prices in real time.
            If no zip code is provided, assume Atlanta, GA.
            
            Choose the store with the lowest total price.
            Output the estimated price and the store name.
            
            If Atlanta was used as the default, add: "(Prices based on Atlanta, GA)"
            """;


    public PlanService(ListRepository listRepository, ModelMapper modelMapper,
                       JsonMapper jsonMapper, GoogleGenAiChatModel googleGenAiChatModel) {
        this.LIST_REPOSITORY = listRepository;
        this.MODEL_MAPPER = modelMapper;
        this.JSON_MAPPER = jsonMapper;
        this.GEMINI_CHAT_MODEL = googleGenAiChatModel;
    }

    public PlanDTO createPlan(Long listId, String zipCode) {
        if (zipCode != null && !zipCode.isEmpty() && !zipCode.matches("\\d{5}")) {
            throw new IllegalEntityException("Invalid zip code format: " + zipCode);
        }
        try {
            ListDTO list = LIST_REPOSITORY.findById(listId)
                    .map(lst ->
                    MODEL_MAPPER.map(lst, ListDTO.class)
                    )
                    .orElseThrow(() -> new EntityNotFoundException("List not found with id: " + listId));
            List<LlmItemDTO> llmItems = list.getItems().stream().map(item -> MODEL_MAPPER.map(item, LlmItemDTO.class)).toList();
            String listString = JSON_MAPPER.writeValueAsString(llmItems);
            LOG.debug("listString: {}", listString);
            LOG.debug("Sending request to Gemini with list and zip code");
            String message = "System Prompt: " + PROMPT + "\nList: " + listString + "\nZip code: " + zipCode;
            try {
                String response = ChatClient.create(GEMINI_CHAT_MODEL)
                        .prompt(message)
                        .options(ToolCallingChatOptions.builder()
                                .model("gemini-3-flash-preview")
                                .build())
                        .call()
                        .content();

                LOG.debug("Received response from gemini");
                if (response == null || response.isEmpty()) {
                    throw new EntityNotActionedException("Gemini returned an empty response");
                }
                LOG.debug("Gemini response: {}", response);
                return new PlanDTO(response);
            } catch (ServerException e) {
                LOG.error("Gemini server error: {}", e.getMessage(), e.getCause());
                throw new EntityNotActionedException(e);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (ConfigurationException | MappingException | OptimisticLockingFailureException e) {
            throw new EntityNotActionedException(e);
        }
    }
}
