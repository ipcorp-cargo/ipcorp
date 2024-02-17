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
            "/api/auth/seller/signup",
            "/api/auth/seller/signin",
            "/api/auth/access-token",
            "/v3/api-docs",
            "/swagger-ui/index.html",
            "/api/products",
            "/api/orders",
            "/api/categories",
            "/api/companies",
            "/api/emails",
            "/api/emails/verify",
            "/api/sms",
            "/api/sms/verify",
            "/api/companies/company",
            "/api/companies/filter",
            "/api/companies/filter/date",
            "/api/companies/filter/name",
            "/api/companies/verify"
    );



    public Predicate<ServerHttpRequest> isSecured = request ->
            OPEN_API_ENDPOINTS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
