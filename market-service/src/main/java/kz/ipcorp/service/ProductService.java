package kz.ipcorp.service;


import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.model.DTO.ProductSaveDTO;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;
    private final CompanyService companyService;
    private final Logger log = LogManager.getLogger(ProductService.class);

    @Transactional
    public void saveProduct(ProductSaveDTO productSaveDTO, UUID id) {

        Seller seller = sellerService.getById(id);
        Company company = seller.getCompany();
        if (company == null || company.getStatus() != Status.ACCEPT) {
            log.error("IN saveProduct - hasn't company or status not accept");
            throw new NotConfirmedException("company not registered or verified");
        }

        Product product = new Product();
        product.setName(productSaveDTO.getName());
        product.setPrice(productSaveDTO.getPrice());
        product.setDescription(productSaveDTO.getDescription());
        product.setImagePaths(productSaveDTO.getImagePaths());
        product.setCompany(company);
        productRepository.save(product);
        company.getProducts().add(product);
        companyService.saveCompany(company);
        log.info("IN saveProduct - productName: {}", product.getName());
    }
}
