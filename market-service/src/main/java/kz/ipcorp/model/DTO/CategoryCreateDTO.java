package kz.ipcorp.model.DTO;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CategoryCreateDTO {
    private String nameKazakh;
    private String nameEnglish;
    private String nameRussian;
    private String nameChinese;
    private MultipartFile iconPath;
}
