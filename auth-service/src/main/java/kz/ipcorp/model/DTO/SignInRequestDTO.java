package kz.ipcorp.model.DTO;


import lombok.Data;

@Data
public class SignInRequestDTO {
    private String phoneNumber;
    private String password;
}
