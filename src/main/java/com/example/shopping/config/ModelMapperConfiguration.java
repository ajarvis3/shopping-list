package com.example.shopping.config;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.model.ItemModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    @Primary
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE); // avoid strange behavior with lazy loads
        modelMapper.typeMap(ItemModel.class, ItemDTO.class)
                .addMappings(mapper -> {
                    mapper.map(
                            src -> src.getListId().getId(),
                            ItemDTO::setListId
                    );
                });
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperTwo(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE); // avoid strange behavior with lazy loads
        modelMapper.typeMap(ItemModel.class, ItemDTO.class)
                .addMappings(mapper -> {
                    mapper.map(
                            src -> src.getListId().getId(),
                            ItemDTO::setListId
                    );
                });
        return modelMapper;
    }


}
