package kz.ipcorp;

import io.swagger.v3.oas.annotations.servers.Servers;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
@io.swagger.v3.oas.annotations.servers.Server(url = "https://api.ipcorpn.com")
public class MarketServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MarketServiceApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        Server server = new Server();
        server.setUrl("https://api.ipcorpn.com");
        return new OpenAPI()
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name(securitySchemeName)
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("controller-api")
                .packagesToScan("kz.ipcorp.controller")
                .build();
    }
}
