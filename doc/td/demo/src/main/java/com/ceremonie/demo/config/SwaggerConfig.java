package com.ceremonie.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "API Gestion Cérémonie Religieuse",
                version = "1.0.0",
                description = "API REST pour la gestion complète des cérémonies religieuses annuelles",
                contact = @Contact(
                        name = "Équipe Développement",
                        email = "support@ceremony.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8086", description = "Serveur de développement"),
                @Server(url = "https://api.ceremony.com", description = "Serveur de production")
        },
        security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Authentification JWT. Entrez le token reçu lors de la connexion."
)
public class SwaggerConfig {
}

