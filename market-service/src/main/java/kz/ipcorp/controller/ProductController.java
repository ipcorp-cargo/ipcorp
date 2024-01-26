package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.ProductSaveDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Logger log = LogManager.getLogger(ProductController.class);
    @PostMapping
    public ResponseEntity<HttpStatus> saveProduct(@RequestBody ProductSaveDTO productSaveDTO, Principal principal) {
        log.info("IN saveProduct - productName: {}, sellerId: {}", productSaveDTO.getName(), UUID.fromString(principal.getName()));
        productService.saveProduct(productSaveDTO, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getProducts(Principal principal) {
        return new ResponseEntity<>(productService.getProducts(UUID.fromString(principal.getName())), HttpStatus.OK);
    }
}
