package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryViewDTO {

    private UUID id;
    private String name;
    private String iconPath;

    public CategoryViewDTO() {
    }


}
