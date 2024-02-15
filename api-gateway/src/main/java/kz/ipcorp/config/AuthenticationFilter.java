package kz.ipcorp.config;

import kz.ipcorp.exception.AuthException;
import kz.ipcorp.service.JWTService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger log = LogManager.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("IN apply");
        return (((exchange, chain) -> {
            log.info("======================");
            log.info(exchange.getRequest().getPath());
            log.info("======================");

            ServerHttpRequest request = null;

            if (router.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new AuthException("ACCESS INVALID");
                }
                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    System.out.println("have token");
                }
                if(!jwtService.isTokenExpired(token)) {
                    log.info("token is not expired");
                    try {
                        request = exchange.getRequest().mutate()
                                .build();
                    } catch (Exception e) {
                        throw new AuthException("ACCESS INVALID");
                    }
                }
            }
            log.info("iwinen wikti");
            return chain.filter(exchange.mutate().request(request).build());
        }));
    }
    public static class Config {
    }
}
