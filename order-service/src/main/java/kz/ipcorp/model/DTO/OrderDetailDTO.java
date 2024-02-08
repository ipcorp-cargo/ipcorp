package kz.ipcorp.model.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderDetailDTO {
    private UUID id;
    private String trackCode;
    private String orderName;
    private List<Map<String, String>> statusList;
    private LocalDateTime time;
}
