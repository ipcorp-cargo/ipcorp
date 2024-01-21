package kz.ipcorp.service;


import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.model.DTO.ProductSaveDTO;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Product;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;
    private final CompanyService companyService;

    @Transactional
    public void saveProduct(ProductSaveDTO productSaveDTO, String email) {

        Seller seller = sellerService.getByEmail(email);
        Company company = seller.getCompany();
        if (company == null || company.getStatus() != Status.ACCEPT) {
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
    }
}
