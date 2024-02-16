package kz.ipcorp.service;

import kz.ipcorp.controller.StatusController;
import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.StatusViewDTO;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.model.entity.Status;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.model.entity.UserStatus;
import kz.ipcorp.model.enumuration.Role;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final UserService userService;
    private final Logger log = LogManager.getLogger(StatusService.class);

    @Transactional(readOnly = true)
    public List<Map<String, String>> getStatus(UUID userId, String language) {
        User user = userService.findById(userId);
        List<Map<String, String>> statuses = new ArrayList<>();
        for (UserStatus userStatus : user.getUserStatuses()) {
            Language statusLanguage = userStatus.getStatus().getLanguage();
            log.info("UserStatus {}", userStatus.getId());
            log.info("Language {}", statusLanguage.getId());
            log.info("Status {}", userStatus.getStatus());
            log.info("Status {}", userStatus.getStatus().getId());
            switch (language) {
                case "kk" -> statuses.add(
                        Map.of(
                                "id", userStatus.getStatus().getId().toString(),
                                "status", statusLanguage.getKazakh(),
                                "description", statusLanguage.getDescriptionKazakh())
                );
                case "en" -> statuses.add(
                        Map.of(
                                "id", userStatus.getStatus().getId().toString(),
                                "status", statusLanguage.getEnglish(),
                                "description", statusLanguage.getDescriptionEnglish())
                );
                case "ru" -> statuses.add(
                        Map.of(
                                "id", userStatus.getStatus().getId().toString(),
                                "status", statusLanguage.getRussian(),
                                "description", statusLanguage.getDescriptionRussian()
                        )
                );
                case "cn" -> statuses.add(
                        Map.of(
                                "id", userStatus.getStatus().getId().toString(),
                                "status", statusLanguage.getChinese(),
                                "description", statusLanguage.getDescriptionChinese()
                        )
                );
            }
        }

        return statuses;

    }


}