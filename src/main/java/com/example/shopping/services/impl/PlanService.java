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
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Service
public class PlanService implements IPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanService.class);

    private final Client GEMINI_CLIENT;

    private final ModelMapper MODEL_MAPPER;

    private final JsonMapper JSON_MAPPER;

    private final ListRepository LIST_REPOSITORY;

    private final GenerateContentConfig CONFIG;

    private static final String CHAT_MODEL = "gemini-3.1-flash-lite-preview";


    public PlanService(Client geminiClient, ListRepository listRepository,
                       ModelMapper modelMapper, JsonMapper jsonMapper, GenerateContentConfig generateContentConfig) {
        this.GEMINI_CLIENT = geminiClient;
        this.LIST_REPOSITORY = listRepository;
        this.MODEL_MAPPER = modelMapper;
        this.JSON_MAPPER = jsonMapper;
        this.CONFIG = generateContentConfig;
    }

    public PlanDTO createPlan(Long listId, String zipCode) {
        if (!zipCode.isEmpty() && !zipCode.matches("\\d{5}")) {
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
            Part listContent = Part.builder()
                    .text("List: " + listString)
                    .build();
            Part zipCodeContent = Part.builder()
                    .text("Zip Code: " + zipCode)
                    .build();

            LOG.debug("Sending request to Gemini with list and zip code");
            try {
                GenerateContentResponse response = GEMINI_CLIENT.models.generateContent(
                        CHAT_MODEL,
                        Content.builder()
                                .parts(List.of(listContent, zipCodeContent))
                                .build(),
                        CONFIG);
                LOG.debug("Received response from gemini");
                if (response == null || response.text() == null || response.text().isEmpty()) {
                    throw new EntityNotActionedException("Gemini returned an empty response");
                }
                LOG.debug("Gemini response: {}", response.text());
                return new PlanDTO(response.text());
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
