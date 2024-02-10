package kz.ipcorp.service;


import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.CategoryCreateDTO;
import kz.ipcorp.model.DTO.CategoryViewDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.model.entity.Product;
import kz.ipcorp.repository.CategoryRepository;
import kz.ipcorp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final LanguageService languageService;

    @Transactional(readOnly = true)
    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("category not found"));
    }

    @Transactional(readOnly = true)
    public List<CategoryViewDTO> getCategories(String language) {

        List<CategoryViewDTO> categoryViewDTOS = new ArrayList<>();

        for (Category category : categoryRepository.findAll()) {
            categoryViewDTOS.add(CategoryViewDTO.builder()
                    .id(category.getId())
                    .iconPath(category.getIconPath())
                    .name(convertLanguage(category.getLanguage(), language))
                    .build());
        }
        return categoryViewDTOS;
    }

    private String convertLanguage(Language language, String acceptLanguage) {
        switch (acceptLanguage) {
            case "en" -> {
                return language.getEnglish();
            }
            case "ru" -> {
                return language.getRussian();
            }
            case "kk" -> {
                return language.getKazakh();
            }
            case "cn" -> {
                return language.getChinese();
            }
            default -> throw new NotFoundException("not found language");
        }
    }


    @Transactional
    public void saveCategory(CategoryCreateDTO categoryCreateDTO, String iconPath) {
        Category category = new Category();

        Language language = Language.builder()
                .kazakh(categoryCreateDTO.getNameKazakh())
                .english(categoryCreateDTO.getNameEnglish())
                .russian(categoryCreateDTO.getNameRussian())
                .chinese(categoryCreateDTO.getNameChinese())
                .build();
        Language savedLanguage = languageService.saveLanguage(language);

        category.setIconPath(iconPath);
        category.setLanguage(savedLanguage);

        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<ProductViewDTO> getProducts(UUID categoryId, String language, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found"));

        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        return productPage.map((product -> new ProductViewDTO(product, language))).getContent();
    }
}
