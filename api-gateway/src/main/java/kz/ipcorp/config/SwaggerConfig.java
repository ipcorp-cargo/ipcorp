package kz.ipcorp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = {
        @Server(url = "lb/user-service"),
        @Server(url = "lb/auth-service")
})
public class SwaggerConfig {
}
