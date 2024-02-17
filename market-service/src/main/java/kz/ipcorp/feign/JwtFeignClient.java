package kz.ipcorp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface JwtFeignClient {
    @GetMapping(path = "api/admin")
    String testMethod(@RequestHeader("Authorization") String token);
}