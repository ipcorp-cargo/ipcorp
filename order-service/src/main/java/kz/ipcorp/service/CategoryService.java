//package kz.ipcorp.service;
//
//import kz.ipcorp.dto.CategoryDTO;
//import kz.ipcorp.dto.CategoryReadDTO;
//import kz.ipcorp.model.entity.Category;
//import kz.ipcorp.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import util.FileManager;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class CategoryService {
//
//    private final CategoryRepository categoryRepository;
//
//    public List<CategoryReadDTO> getAll(){
//        List<CategoryReadDTO> categoryReadDTOList = new ArrayList<>();
//        for(Category category : categoryRepository.findAll()){
//            categoryReadDTOList.add(new CategoryReadDTO(category.getId(), category.getCategoryName()));
//        }
//        return categoryReadDTOList;
//    }
//
//    @Transactional
//    public void addCategory(String categoryName, String filePath) {
//        Category category = new Category();
//        category.setCategoryName(categoryName);
//        category.setIconPath(filePath);
//        categoryRepository.save(category);
//    }
//
//    @Transactional
//    public byte[] getIconById(UUID id) {
//
//        return null;
//    }
//}