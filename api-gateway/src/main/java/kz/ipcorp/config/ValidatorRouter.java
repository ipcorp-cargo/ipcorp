package kz.ipcorp.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class ValidatorRouter {

    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/api/auth/signup",
            "/api/auth/signin",
            "/api/auth/access-token",
            "/v3/api-docs",
            "/swagger-ui/index.html",
            "/api/products",
            "/api/orders"
    );



    public Predicate<ServerHttpRequest> isSecured = request ->
            OPEN_API_ENDPOINTS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
