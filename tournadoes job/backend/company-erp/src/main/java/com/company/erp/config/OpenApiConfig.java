package com.company.erp.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3 / Swagger UI configuration.
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Authorization header. Provide: Bearer {token}"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Company ERP API")
                        .description("Enterprise Resource Planning — REST API Documentation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Company IT Team")
                                .email("it@company.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://company.com")))
                .servers(List.of(
                        new Server().url("/api").description("Default server")
                ));
    }
}
