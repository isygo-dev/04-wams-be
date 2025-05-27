package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.model.Category;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = CategoryRepository.class)
@DmsLinkFileService(DmsLinkedFileService.class)
public class CategoryService extends ImageService<Long, Category, CategoryRepository> implements ICategoryService {

    private final AppProperties appProperties;

    @Autowired
    private TemplateRepository templateRepository;

    public CategoryService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public Map<Long, Integer> countTemplatesByCategory() {
        Map<Long, Integer> templateCountMap = templateRepository.countByCategory().stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> ((Long) result[1]).intValue()
                ));

        templateCountMap.put(null, templateRepository.countWithoutCategory());

        return templateCountMap;
    }

    @Override
    public int countTemplatesForCategory(Long categoryId) {
        if (categoryId == null) {
            return (int) templateRepository.count();
        }
        return templateRepository.countByCategoryId(categoryId);
    }
}
