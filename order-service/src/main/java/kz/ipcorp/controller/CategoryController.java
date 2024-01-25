package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.CategoryReadDTO;
import kz.ipcorp.feign.IconFeignClient;
import kz.ipcorp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final IconFeignClient iconFeignClient;
    private final Logger log = LogManager.getLogger(CategoryController.class);
    @GetMapping
    public ResponseEntity<List<CategoryReadDTO>> getAll(@RequestHeader(value = "username", required = false) String username){
        log.info("IN getAll - username: {}", username);
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCategory(@RequestParam("categoryName") String categoryName,
                                         @RequestParam("icon") MultipartFile icon){
        log.info("IN addCategory - categoryName: {}", categoryName);
        String filePathname = iconFeignClient.getPathName(icon);
        categoryService.addCategory(categoryName, filePathname);
        return new ResponseEntity<>(filePathname, HttpStatus.CREATED);
    }

}
