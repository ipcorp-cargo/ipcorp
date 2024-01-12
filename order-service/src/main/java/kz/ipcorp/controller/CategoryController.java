package kz.ipcorp.controller;

import kz.ipcorp.dto.CategoryDTO;
import kz.ipcorp.dto.CategoryReadDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.rest.ImageWebBuilder;
import kz.ipcorp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ImageWebBuilder imageWebBuilder;
    @GetMapping
    public ResponseEntity<List<CategoryReadDTO>> getAll(){
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName,
                                         @RequestParam("icon") MultipartFile icon) throws IOException {
        System.out.println(icon.getOriginalFilename());
        imageWebBuilder.imagePutAsync(icon).subscribe();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{id}/icon")
    public ResponseEntity<byte[]> findIcon(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(categoryService.getIconById(id), HttpStatus.OK);
    }

}