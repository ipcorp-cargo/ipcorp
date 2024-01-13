package kz.ipcorp.controller;

import kz.ipcorp.dto.CategoryDTO;
import kz.ipcorp.dto.CategoryReadDTO;
import kz.ipcorp.feign.IconFeignClient;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final IconFeignClient iconFeignClient;
    @GetMapping
    public ResponseEntity<List<CategoryReadDTO>> getAll(){
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCategory(@RequestParam("categoryName") String categoryName,
                                         @RequestParam("icon") MultipartFile icon){
        String filePathname = iconFeignClient.getPathName(icon);
        categoryService.addCategory(categoryName, filePathname);
        return new ResponseEntity<>(filePathname, HttpStatus.CREATED);
    }

    @GetMapping("/getByCategoryName")
    public ResponseEntity<CategoryReadDTO> findIcon(@RequestParam("categoryName") String categoryName) {
        return new ResponseEntity<>(categoryService.getByCategoryName(categoryName), HttpStatus.OK);
    }

}