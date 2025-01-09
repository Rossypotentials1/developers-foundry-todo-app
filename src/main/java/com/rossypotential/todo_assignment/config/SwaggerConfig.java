package com.rossypotential.todo_assignment.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TODO Assignment Application API",
                description = "API Endpoints for TODO App Assignment, providing all necessary operations for managing users, authentication, and more.",
                version = "1.0",
                contact = @Contact(
                        name = "Developers Foundry Assignment",
                        email = "urokorosemarynnena@gmail.com"
                ),
                license = @License(
                        name = "TODO App License",
                        url = "https://github.com/rossypotentials1"
                )
        ),
        servers = {
                @Server(
                        url = "https://devfoundry-todo-7a5ac952823c.herokuapp.com",
                        description = "Production Server"
                ),
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        },
        externalDocs = @ExternalDocumentation(
                description = "RESTful API Documentation for TODO App",
                url = "https://github.com/rossypotentials1"        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication using Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}