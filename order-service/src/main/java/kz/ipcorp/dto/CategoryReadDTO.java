package kz.ipcorp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CategoryReadDTO {
    private UUID uuid;
    private String name;
}
