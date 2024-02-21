package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import kz.ipcorp.exception.NotConfirmedException;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@ToString(exclude = "company")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;
    @Column(name = "description")
    private String description;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "product_paths", joinColumns = @JoinColumn(name = "image_path_id"))
    @Column(name = "imagePath")
    private List<String> imagePaths = new ArrayList<>();

    @ManyToOne
    private Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void deleteImagePath(UUID sellerId, String imagePath) {
        if (!company.getSeller().getId().equals(sellerId)) {
            throw new NotConfirmedException("no access for delete image");
        }
        this.imagePaths.removeIf(path -> path.equals(imagePath));
    }
}
