package kz.ipcorp.model.DTO;

import jakarta.persistence.*;
import kz.ipcorp.model.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductViewDTO {
    private UUID id;
    private String name;

    private Integer price;
    private String description;
    private List<String> imagePaths = new ArrayList<>();
    private String categoryName;

    public ProductViewDTO() {
    }

    public ProductViewDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imagePaths = new ArrayList<>(product.getImagePaths());
        if (product.getCategory() != null) {
//            TODO: do conca
            this.categoryName = product.getCategory().getLanguage().getEnglish();
        }
    }
}
