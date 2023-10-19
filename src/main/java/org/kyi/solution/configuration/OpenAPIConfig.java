package org.kyi.solution.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${g4.openapi.dev-url}")
    private String devUrl;

    @Value("${g4.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development Environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production Environment");

        Contact contact = new Contact();
        contact.setEmail("hha.heinms@gmail.com");
        contact.setName("G4");
        contact.setUrl("https://g4backend.onrender.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Backend API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage.")
                .license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
