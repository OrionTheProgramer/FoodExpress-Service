package com.servicios.FoodExpress.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "FoodExpress API Pedidos", version = "1.0", description = "API para gestionar pedidos en FoodExpress"))
public class OpenApiConfig {
}
