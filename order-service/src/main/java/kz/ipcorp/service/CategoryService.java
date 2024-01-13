package kz.ipcorp.service;

import kz.ipcorp.dto.CategoryReadDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryReadDTO> getAll(){
        List<CategoryReadDTO> categoryReadDTOList = new ArrayList<>();
        for(Category category : categoryRepository.findAll()){
            categoryReadDTOList.add(new CategoryReadDTO(category.getCategoryName(), category.getIconPath()));
        }
        return categoryReadDTOList;
    }

    @Transactional
    public void addCategory(String categoryName, String filePath) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setIconPath(filePath);
        categoryRepository.save(category);
    }
    public CategoryReadDTO getByCategoryName(String categoryName) {
        Optional<Category> category = categoryRepository.getByCategoryName(categoryName);
        return category.map(value -> new CategoryReadDTO(value.getCategoryName(), value.getIconPath())).orElseGet(CategoryReadDTO::new);
    }
}