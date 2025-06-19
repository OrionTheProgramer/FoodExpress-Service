package com.servicios.FoodExpress.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Cocina/Menu", version = "v1", description = "API para la gestión de menús y platos en el servicio de cocina de FoodExpress"))
public class OpenApiConfig {
}
