package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Category;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByCompany(Company company, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
}
