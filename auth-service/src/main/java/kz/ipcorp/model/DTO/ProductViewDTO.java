package kz.ipcorp.model.DTO;

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
    private String companyName;
}
