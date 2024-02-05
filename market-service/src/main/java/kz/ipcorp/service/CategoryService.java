package kz.ipcorp.service;


import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.CategoryCreateDTO;
import kz.ipcorp.model.DTO.CategoryViewDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.model.entity.Product;
import kz.ipcorp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("category not found"));
    }
    @Transactional(readOnly = true)
    public List<CategoryViewDTO> getCategories() {
        List<CategoryViewDTO> categories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categories.add(new CategoryViewDTO(category));
        }
        return categories;
    }


    @Transactional
    public CategoryViewDTO saveCategory(CategoryCreateDTO categoryCreateDTO, String iconPath) {
        Category category = new Category();
        category.setIconPath(iconPath);
        category.setName(categoryCreateDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryViewDTO(savedCategory);
    }
    @Transactional(readOnly = true)
    public List<ProductViewDTO> getProducts(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found"));

        List<ProductViewDTO> productViewDTOS = new ArrayList<>();
        for (Product product : category.getProducts()) {
            productViewDTOS.add(new ProductViewDTO(product));
        }

        return productViewDTOS;
    }
}
