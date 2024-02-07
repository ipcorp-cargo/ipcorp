package kz.ipcorp.controller;


import jakarta.ws.rs.Path;
import kz.ipcorp.feign.MediaFeignClient;
import kz.ipcorp.model.DTO.CategoryCreateDTO;
import kz.ipcorp.model.DTO.CategoryViewDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final MediaFeignClient mediaFeignClient;
    private final Logger log = LogManager.getLogger(CategoryController.class);


    @GetMapping
    public ResponseEntity<List<CategoryViewDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryViewDTO> createCategory(@ModelAttribute CategoryCreateDTO categoryCreateDTO) {
        log.info("createCategory");
        String iconPath = mediaFeignClient.getPathCategoryIcon(categoryCreateDTO.getIconPath());
        System.out.println("============================");
        System.out.println(iconPath);
        System.out.println("============================");
        log.info("iconPath {}", iconPath);
        return new ResponseEntity<>(categoryService.saveCategory(categoryCreateDTO, iconPath), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<ProductViewDTO>> getProducts(@PathVariable("categoryId") UUID categoryId) {
        return ResponseEntity.ok(categoryService.getProducts(categoryId));
    }
}
