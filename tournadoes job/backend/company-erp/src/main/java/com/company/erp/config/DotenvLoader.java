package com.company.erp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads environment variables from .env file and makes them available to Spring.
 */
public class DotenvLoader implements EnvironmentPostProcessor {

    private static final String DOTENV_PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> dotenvProperties = new HashMap<>();
        
        try {
            // Look for .env in the project root (backend/company-erp/)
            Path projectRoot = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
            
            Dotenv dotenv = Dotenv.configure()
                    .directory(projectRoot.toString())
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry -> {
                dotenvProperties.put(entry.getKey(), entry.getValue());
            });
            
            environment.getPropertySources().addFirst(new MapPropertySource(DOTENV_PROPERTY_SOURCE_NAME, dotenvProperties));
            
            System.out.println(".env file loaded successfully from: " + projectRoot.toString());
            
        } catch (Exception e) {
            // .env file not found or error loading - continue without it
            System.out.println("No .env file found or error loading it. Using default configuration.");
        }
    }
}
