package kz.ipcorp.controller;

import kz.ipcorp.feign.MediaFeignClient;
import kz.ipcorp.model.DTO.ProductSaveDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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
        return new ResponseEntity<>(productId,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getProducts(Principal principal) {
        if (principal != null) {
            return new ResponseEntity<>(productService.getProducts(UUID.fromString(principal.getName())), HttpStatus.OK);
        }
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping(path = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveImage(@PathVariable("productId") UUID productId,
                                       @RequestParam("image") MultipartFile image){
        String path = mediaFeignClient.getPath(image);
        productService.saveImagePath(productId, path);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(Principal principal,
                                                    @RequestHeader(name = "userId", required = false) UUID adminId,
                                                    @PathVariable("productId") UUID productId) {
        productService.deleteProduct(UUID.fromString(principal.getName()), adminId, productId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
