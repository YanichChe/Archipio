package ru.archipio;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Archipio",
                description = "Architecture portfolio", version = "1.0.0",
                contact = @Contact(
                        name = "Chernovskaya Yana",
                        email = "ychernovskaya@gmail.com"
                )
        )
)

@Configuration
public class OpenApiConfig {
}
