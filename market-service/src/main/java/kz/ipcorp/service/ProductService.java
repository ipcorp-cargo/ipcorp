package kz.ipcorp.service;


import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.ProductSaveDTO;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.model.entity.Category;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Product;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final Logger log = LogManager.getLogger(ProductService.class);

    @Transactional
    public UUID saveProduct(ProductSaveDTO productSaveDTO, UUID id) {

        Seller seller = sellerService.getById(id);
        Company company = seller.getCompany();
        if (company == null || company.getStatus() != Status.ACCEPT) {
            log.error("IN saveProduct - hasn't company or status not accept");
            throw new NotConfirmedException("company not registered or verified");
        }

        if (company.getExpiredAt() == null || company.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new NotConfirmedException("company's status expired");
        }

        Product product = new Product();
        product.setName(productSaveDTO.getName());
        product.setPrice(productSaveDTO.getPrice());
        product.setDescription(productSaveDTO.getDescription());
        product.setCompany(company);
        Category category = categoryService.findById(productSaveDTO.getCategoryId());
        product.setCategory(category);
        productRepository.save(product);
        company.getProducts().add(product);
        companyService.saveCompany(company);
        log.info("IN saveProduct - productName: {}", product.getName());
        return product.getId();
    }

    @Transactional(readOnly = true)
    public List<ProductViewDTO> getProducts(UUID sellerId) {
        Seller seller = sellerService.getById(sellerId);
        Company company = seller.getCompany();

        checkCompanyStatus(company);

        List<ProductViewDTO> products = new ArrayList<>();
        System.out.println("=====================");
        System.out.println(company.getProducts());
        System.out.println("=====================");
        for (Product product : company.getProducts()) {
            products.add(new ProductViewDTO(product));
        }

        return products;
    }

    private void checkCompanyStatus(Company company) {
        if (company == null) {
            throw new NotFoundException("company is not registered");
        }
        switch (company.getStatus()) {
            case NOT_UPLOADED -> throw new NotConfirmedException("licence is not uploaded");
            case UPLOADED -> throw new NotConfirmedException("licence is not accepted");
            case DENY ->  throw new NotConfirmedException("licence status denied");
        }
    }

    @Transactional
    public void saveImagePath(UUID productId,String path) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(""));

        if (product.getImagePaths().size() >= 10) {
            throw new NotConfirmedException("limit 10 image for product");
        }

        product.getImagePaths().add(path);
        productRepository.save(product);
    }
}
