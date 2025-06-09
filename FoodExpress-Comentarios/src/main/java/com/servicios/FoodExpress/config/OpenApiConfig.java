package com.servicios.FoodExpress.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FoodExpress Comentarios API",
                version = "1.0",
                description = "API para gestionar los comentarios de los productos en FoodExpress"
        ))
public class OpenApiConfig {
}
