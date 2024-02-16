package kz.ipcorp.service;
/**
 * change status by track code
 * pagination for containers
 * by date
 * by name
 * creation_date
 * company pagination
 */

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public List<ProductViewDTO> getAllProducts(String language, Pageable pageable){
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map((product -> new ProductViewDTO(product, language))).getContent();
    }

    @Transactional(readOnly = true)
    public List<ProductViewDTO> getProducts(UUID sellerId, String language, Pageable pageable) {
        Seller seller = sellerService.getById(sellerId);
        Company company = seller.getCompany();

        checkCompanyStatus(company);

        Page<Product> productPage = productRepository.findByCompany(company, pageable);
        return productPage.map((product -> new ProductViewDTO(product, language))).getContent();
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

    @Transactional
    public void deleteProduct(UUID sellerId, UUID adminId, UUID productId) {
        boolean isDeletedWithSeller = deleteProductOwnerSeller(sellerId, productId);
        boolean isDeletedWithAdmin = deleteProductWithAdmin(adminId, productId);
        if (!isDeletedWithSeller && !isDeletedWithAdmin) {
            throw new NotConfirmedException("you can not delete order. Only owner product can access");
        }
    }

    private boolean deleteProductWithAdmin(UUID adminId, UUID productId) {
//        TODO: admin can delete product if he has role admin
//        if(adminId != null) {
//            throw new NotConfirmedException("poka tolko seller delete jasai aladi");
//        }
        return false;
    }

    private boolean deleteProductOwnerSeller(UUID sellerId, UUID productId) {
        Optional<Seller> seller = sellerService.findById(sellerId);
        if(sellerId == null || seller.isEmpty()) {
            return false;
        } else {
            Seller owner = seller.get();

            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalProduct.isEmpty()) {
                throw new NotFoundException(String.format("product with id %s not found", productId));
            } else {
                Product product = optionalProduct.get();
                if (product.getCompany().getSeller().getId().equals(owner.getId())) {
//                    TODO: есть уязвимость что в жаве классе останеться product
                    productRepository.deleteById(productId);
                }
            }
            return true;
        }
    }
    @Transactional(readOnly = true)
    public List<ProductViewDTO> getProductsByCategory(UUID categoryId, String language, PageRequest pageable) {
        Category category = categoryService.findById(categoryId);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        return productPage.map((product -> new ProductViewDTO(product, language))).getContent();
    }


    @Transactional
    public ProductViewDTO getProduct(UUID productId, String language) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("product not found"));
        return new ProductViewDTO(product, language);
    }

}
