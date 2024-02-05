package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryViewDTO {

    private UUID id;
    private String name;

    private String iconPath;

    public CategoryViewDTO() {
    }

    public CategoryViewDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.iconPath = category.getIconPath();
    }
}
