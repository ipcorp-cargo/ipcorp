package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/favorite")
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;

    @PostMapping("/{product_id}")
    public ResponseEntity<HttpStatus> addFavoriteProduct(Principal principal, @PathVariable("product_id") UUID product_id) {
        userService.addFavoriteProduct(UUID.fromString(principal.getName()), product_id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getFavoriteProduct(Principal principal, @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        return userService.getFavoriteProducts(UUID.fromString(principal.getName()), language);
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<HttpStatus> deleteFavoriteProduct(Principal principal, @PathVariable("product_id") UUID productId) {
        userService.deleteFavoriteProduct(UUID.fromString(principal.getName()), productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<Boolean> isFavoriteProduct(Principal principal,
                                                     @PathVariable("product_id") UUID productId) {
        return ResponseEntity.ok(userService.isFavoriteProduct(UUID.fromString(principal.getName()), productId));
    }
}
