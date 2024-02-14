package kz.ipcorp.controller;

import kz.ipcorp.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class StatusController {

    private final StatusService statusService;
    private final Logger log = LogManager.getLogger(StatusController.class);
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getStatuses(@CookieValue(name = "Accept-Language", defaultValue = "ru") String language,
                                                                 Principal principal) {
        log.info("StatusController IN getStatuses {} Accept-Language {}", principal.getName(), language);
        return ResponseEntity.ok(statusService.getStatus(UUID.fromString(principal.getName()), language));
    }

}
