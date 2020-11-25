package com.epam.esm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages = {"com"})
@EntityScan( basePackages = {"com.epam.esm.model", "com.epam.esm.audit.entity"} )
@Configuration
public class SpringConfig implements WebMvcConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Profile("test")
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}