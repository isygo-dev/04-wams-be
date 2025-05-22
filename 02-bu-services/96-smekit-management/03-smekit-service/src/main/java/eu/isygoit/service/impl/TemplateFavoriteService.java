package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.model.Template;
import eu.isygoit.model.TemplateFavorite;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.templateFavoriteRepository;
import eu.isygoit.repository.TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TemplateRepository.class)
public class TemplateFavoriteService {
    private final templateFavoriteRepository favoriteRepository;
    private final TemplateRepository templateRepository;

    public TemplateFavoriteService(templateFavoriteRepository favoriteRepository, TemplateRepository templateRepository) {
        this.favoriteRepository = favoriteRepository;
        this.templateRepository = templateRepository;
    }

    @Transactional
    public boolean toggleFavorite(Long templateId, RequestContextDto context) {
        String userIdentifier = context.getSenderUser();

        if (favoriteRepository.existsByUserIdentifierAndTemplateId(userIdentifier, templateId)) {
            favoriteRepository.deleteByUserAndTemplate(userIdentifier, templateId);
            return false;
        } else {
            Template template = templateRepository.findById(templateId)
                    .orElseThrow(() -> new RuntimeException("Template not found with id: " + templateId));

            TemplateFavorite favorite = TemplateFavorite.builder()
                    .userIdentifier(userIdentifier)
                    .template(template)
                    .domain(context.getSenderDomain())
                    .build();

            favoriteRepository.save(favorite);
            return true;
        }
    }

    public boolean isFavorite(String userIdentifier, Long templateId) {
        if (userIdentifier == null || templateId == null) {
            return false;
        }
        return favoriteRepository.isTemplateFavorite(userIdentifier, templateId);
    }

    public List<Template> getUserFavorites(String userIdentifier) {
        return favoriteRepository.findByUserIdentifier(userIdentifier)
                .stream()
                .map(TemplateFavorite::getTemplate)
                .toList();
    }

    public long countByUser(String userIdentifier) {
        if (userIdentifier == null || userIdentifier.isEmpty()) {
            log.warn("Tentative de comptage avec userIdentifier null/vide");
            return 0L;
        }
        return favoriteRepository.countByUserIdentifier(userIdentifier);
    }


}
