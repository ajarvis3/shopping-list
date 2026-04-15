package com.example.shopping;

import org.springframework.context.annotation.Bean;

public class GeminiConfig {

    @Bean
    public GeminiClient geminiConfig() {
        return new GeminiConfig();
    }
}
