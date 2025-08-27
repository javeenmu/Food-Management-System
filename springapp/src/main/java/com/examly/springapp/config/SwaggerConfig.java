package com.examly.springapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

        private SecurityScheme createApiKeyScheme() {
                return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer");

        }

        @Bean
        public OpenAPI createOpenApi() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Food-App")
                                                .version("1.0.0")
                                                .description("Food Delivery Application"))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("Bearer Authentication"))
                                .components(new Components().addSecuritySchemes("Bearer Authentication",
                                                createApiKeyScheme()));
        }
}