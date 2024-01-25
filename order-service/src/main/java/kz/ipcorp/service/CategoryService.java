package kz.ipcorp.service;

import kz.ipcorp.model.DTO.CategoryReadDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger log = LogManager.getLogger(CategoryService.class);

    public List<CategoryReadDTO> getAll(){
        log.info("IN getAll - get all categories");
        List<CategoryReadDTO> categoryReadDTOList = new ArrayList<>();
        for(Category category : categoryRepository.findAll()){
            categoryReadDTOList.add(new CategoryReadDTO(category.getCategoryName(), category.getIconPath()));
        }
        return categoryReadDTOList;
    }

    @Transactional
    public void addCategory(String categoryName, String filePath) {
        log.info("IN addCategory - categoryName: {}, filePath: {}",categoryName, filePath);
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setIconPath(filePath);
        categoryRepository.save(category);
    }
}
