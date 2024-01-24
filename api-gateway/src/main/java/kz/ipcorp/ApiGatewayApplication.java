package kz.ipcorp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@OpenAPIDefinition(
		info= @Info(title = "IP corp endpoints"),
		servers = {
		@Server(url = "http://localhost:8081"),
		@Server(url = "http://localhost:8082"),
		@Server(url = "http://localhost:8085"),
		@Server(url = "http://localhost:8083")
})
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
