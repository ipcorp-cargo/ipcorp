package kz.ipcorp.config;

import kz.ipcorp.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private ValidatorRouter router;

    @Autowired
    private JWTService jwtService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            ServerHttpRequest request = null;
            System.out.println("=======================");
            System.out.println("path: "+exchange.getRequest().getURI().getPath());
            System.out.println("=======================");
            if (router.isSecured.test(exchange.getRequest())) {
                System.out.println("==============================");
                System.out.println("iwine kirdi");
                System.out.println("==============================");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);

                }

                try {
//                    String uri = UriComponentsBuilder.fromUriString("http://auth-service/api/auth")
//                            .queryParam("token", token)
//                            .encode()
//                            .toUriString();
//                    restTemplate.getForEntity(uri, Boolean.class);
                    System.out.println(jwtService.isTokenExpired(token));

                    request = exchange.getRequest().mutate()
                            .header("userId", jwtService.extractID(token))
                            .build();


                } catch (Exception e) {
                    throw new RuntimeException("Access invalid");
                }

            }

            return chain.filter(exchange.mutate().request(request).build());
        }));
    }

    public static class Config {
    }
}
