package com.orelit.springcore.persistence.converter;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    /**
     * Define a bean for ModelMapper, a powerful mapping library that simplifies
     * object-to-object mapping.
     *
     * @return A configured ModelMapper instance.
     */

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
}
