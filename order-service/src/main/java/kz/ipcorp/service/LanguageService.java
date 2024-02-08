package kz.ipcorp.service;


import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public Language getLanguage(Long id) {
        return languageRepository.findById(id).orElseThrow(() -> new NotFoundException("language not found"));
    }
}
