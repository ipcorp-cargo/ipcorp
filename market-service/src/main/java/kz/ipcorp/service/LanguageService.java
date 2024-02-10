package kz.ipcorp.service;


import kz.ipcorp.model.entity.Language;
import kz.ipcorp.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;


    @Transactional
    public Language saveLanguage(Language language) {
        return languageRepository.save(language);
    }
}
