package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.StatusViewDTO;
import kz.ipcorp.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;
    private final Logger log = LogManager.getLogger(StatusController.class);
    @GetMapping
    public ResponseEntity<List<StatusViewDTO>> getStatuses(@RequestHeader("userId") UUID userId,
                                                           @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        log.info("StatusController IN getStatuses {} Accept-Language {}", userId, language);
        return ResponseEntity.ok(statusService.getStatus(userId, language));
    }

}
