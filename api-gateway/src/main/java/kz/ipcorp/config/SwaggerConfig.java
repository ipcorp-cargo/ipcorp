package kz.ipcorp.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    //    @Bean
//    public OpenAPI customOpenApi() {
//        return new OpenAPI()
//                .info(
//                        new Info().title("api-gateway")
//                                .version("1.0")
//                                .description("ipcorp endpoints")
//                );
//    }
    @Bean
    public SpringDocConfiguration springDocConfiguration() {
        return new SpringDocConfiguration();
    }

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name(securitySchemeName)
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("ticket")
//                .packagesToScan("com.springcloud.ticketservice.controller")
//                .build();
//    }
//    @Bean
//    public OpenAPI orderOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("cloud API")
//                        .description("cloud API")
//                        .version("1.0")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
//    }

}
