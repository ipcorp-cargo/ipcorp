package kz.ipcorp.config;

import kz.ipcorp.exception.AuthException;
import kz.ipcorp.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


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
            System.out.println("======================");
            System.out.println(exchange.getRequest().getPath());
            System.out.println("======================");
            System.out.println("======================");
            System.out.println(exchange.getRequest().getCookies().get("refreshToken"));
            System.out.println("======================");
            ServerHttpRequest request = null;
            if (router.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new AuthException("ACCESS INVALID");
                }
                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                try {
                    request = exchange.getRequest().mutate()
                            .header("userId", jwtService.extractID(token))
                            .build();
                } catch (Exception e) {
                    throw new AuthException("ACCESS INVALID");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        }));
    }
    public static class Config {
    }
}
