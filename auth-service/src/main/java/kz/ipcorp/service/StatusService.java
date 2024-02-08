package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.StatusViewDTO;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.model.entity.UserStatus;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<StatusViewDTO> getStatus(UUID userId, String language) {
        User user = userService.findById(userId);

        List<StatusViewDTO> statusViewDTOList = new ArrayList<>();

        for (UserStatus userStatus : user.getUserStatuses()) {
            Language acceptLanguage = userStatus.getStatus().getLanguage();

            String returnLanguage = switch (language) {
                case "kk": {
                    yield acceptLanguage.getKazakh();
                }
                case "ru": {
                    yield acceptLanguage.getRussian();
                }
                case "cn": {
                    yield acceptLanguage.getChinese();
                }
                case "en": {
                    yield acceptLanguage.getEnglish();
                }
                default:
                    throw new NotFoundException("Unexpected language value: " + language);
            };
//TODO: doconca description delay
            statusViewDTOList.add(
                    StatusViewDTO.builder().
                            id(userStatus.getStatus().getId())
                            .status(returnLanguage)
                            .description(null)
                            .build()
            );

        }
        return statusViewDTOList;
    }
}