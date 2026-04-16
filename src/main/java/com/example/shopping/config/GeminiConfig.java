package com.example.shopping.config;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GeminiConfig {

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

    @Bean
    public Client geminiClient() {
        return new Client();
    }

    @Bean
    public GenerateContentConfig generateContentConfig() {
        return GenerateContentConfig.builder()
                .systemInstruction(
                        Content.builder()
                                .parts(Part.builder().text(PROMPT).build())
                )
                .temperature(0.2f)
                .maxOutputTokens(1024)
                .build();
    }
}
