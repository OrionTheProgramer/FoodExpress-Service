package com.servicios.FoodExpress.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Productos", version = "v1", description = "Servicios para gestionar productos de FoodExpress"))
public class OpenApiConfig {
}
