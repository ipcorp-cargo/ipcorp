package kz.ipcorp.model.DTO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ProductIdsWrapper {
    private String language;
    private List<UUID> productIds;
}
