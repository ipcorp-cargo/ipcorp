package kz.ipcorp.feign;


import kz.ipcorp.model.DTO.ProductIdsWrapper;
import kz.ipcorp.model.DTO.ProductViewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "market-service")
public interface UserFeignClient {

    @GetMapping("/api/products/user/favorite")
    ResponseEntity<List<ProductViewDTO>> getFavoriteProducts(@RequestParam("language") String language,
                                                             @RequestParam("productIds") List<UUID> productIds);
}
