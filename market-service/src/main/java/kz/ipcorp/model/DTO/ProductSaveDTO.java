package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSaveDTO {

    private String name;
    private Integer price;
    private String description;

    private List<String> imagePaths;
}
