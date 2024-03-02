package kz.ipcorp.controller;

import kz.ipcorp.feign.MediaFeignClient;
import kz.ipcorp.model.DTO.ProductIdsWrapper;
import kz.ipcorp.model.DTO.ProductSaveDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MediaFeignClient mediaFeignClient;
    private final Logger log = LogManager.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<UUID> saveProduct(@RequestBody ProductSaveDTO productSaveDTO, Principal principal) {
        log.info("IN saveProduct - productName: {}, sellerId: {}", productSaveDTO.getName(), UUID.fromString(principal.getName()));
        UUID productId = productService.saveProduct(productSaveDTO, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getProducts(
            @CookieValue(name = "Accept-Language", defaultValue = "ru") String language,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Principal principal) {
        if (principal != null) {
            return new ResponseEntity<>(productService.getProducts(UUID.fromString(principal.getName()), language, PageRequest.of(page, size)), HttpStatus.OK);
        }
        return new ResponseEntity<>(productService.getAllProducts(language, PageRequest.of(page, size)), HttpStatus.OK);
    }



    @GetMapping("/{productId}")
    public ResponseEntity<ProductViewDTO> getProduct(@PathVariable("productId") UUID productId,
                                                     @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        return ResponseEntity.ok(productService.getProduct(productId, language));
    }

    @GetMapping("/user/favorite")
    public ResponseEntity<List<ProductViewDTO>> getFavoriteProducts(@RequestParam("language") String language,
                                                                    @RequestParam("productIds") List<UUID> productIds) {
        log.info("{}, {}", language, productIds);
        return ResponseEntity.ok(productService.getFavoriteProducts(productIds, language));
    }


    @PostMapping(path = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> saveImage(@PathVariable("productId") UUID productId,
                                                @RequestParam("image") MultipartFile image) {
        String path = mediaFeignClient.getPath(image);
        productService.saveImagePath(productId, path);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/image")
    public ResponseEntity<HttpStatus> deleteImage(Principal principal,
                                                  @PathVariable("productId") UUID productId,
                                                  @RequestParam("imagePath") String path) {
        productService.deleteImageProduct(UUID.fromString(principal.getName()), productId, path);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductViewDTO>> getProductByName(@RequestParam("productName") String productName,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        return ResponseEntity.ok(productService.getProductByName(productName, PageRequest.of(page, size), language));
    }


    @GetMapping("/filter")
    public ResponseEntity<List<ProductViewDTO>> getProductsByCategory(
            @RequestParam("categoryId") UUID categoryId,
            @CookieValue(name = "Accept-Language", defaultValue = "ru") String language,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, language, PageRequest.of(page, size)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("productId") UUID productId,
                                                    Principal principal) {
        productService.deleteProduct(UUID.fromString(principal.getName()), null, productId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
