package com.proyect.web.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desarrollo");

        return new OpenAPI()
                .info(new Info()
                        .title("API de Mi Aplicación")
                        .description("Documentación de la API REST")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@email.com")
                                .url("https://tuempresa.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(localServer));
    }
}