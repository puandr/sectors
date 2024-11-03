package com.example.sectors_app.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sectors API")
                        .version("1.0")
                        .description("API for uploading and retrieving information about user sectors")
                        .contact(new Contact()
                                .name("Andrei Pugatšov")
                                .email("email")
                                .url("URL")));
    }
}
