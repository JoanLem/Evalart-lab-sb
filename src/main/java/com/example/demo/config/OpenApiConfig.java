package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wrapper XML JSON API")
                        .description("API para transformar pedidos JSON a XML, invocar un endpoint externo y devolver respuesta en JSON.")
                        .version("1.0.0")
                        .contact(new Contact().name("Equipo Backend")));
    }
}
