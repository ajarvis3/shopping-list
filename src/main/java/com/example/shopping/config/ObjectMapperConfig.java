package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public JsonMapper jsonMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();
        return builder
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

}
