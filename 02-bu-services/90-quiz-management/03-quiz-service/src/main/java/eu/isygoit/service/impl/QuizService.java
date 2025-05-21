package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.exception.EmptyPathException;
import eu.isygoit.exception.ObjectNotFoundException;
import eu.isygoit.exception.ResourceNotFoundException;
import eu.isygoit.helper.FileHelper;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Quiz;
import eu.isygoit.model.QuizQuestion;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.QuizQuestionRepository;
import eu.isygoit.repository.QuizRepository;
import eu.isygoit.service.IQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The type Quiz service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = QuizRepository.class)
public class QuizService extends CodeAssignableService<Long, Quiz, QuizRepository> implements IQuizService {

    private final AppProperties appProperties;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    /**
     * Instantiates a new Quiz service.
     *
     * @param appProperties the app properties
     */
    public QuizService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Quiz.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("QIZ")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    @Override
    public Resource downloadQuestionImage(Long id) throws MalformedURLException {
        Optional<QuizQuestion> optional = quizQuestionRepository.findById(id);
        if (optional.isPresent()) {
            if (StringUtils.hasText(optional.get().getImagePath())) {
                Resource resource = new UrlResource(Path.of(optional.get().getImagePath()).toUri());
                if (!resource.exists()) {
                    throw new ResourceNotFoundException("for path " + optional.get().getImagePath());
                }
                return resource;
            } else {
                throw new EmptyPathException("for id " + id);
            }
        } else {
            throw new ResourceNotFoundException("with id " + id);
        }
    }

    @Override
    public QuizQuestion uploadQuestionImage(Long id, String domain, MultipartFile file) throws IOException {
        Optional<QuizQuestion> optional = quizQuestionRepository.findById(id);
        if (optional.isPresent()) {
            if (file != null && !file.isEmpty()) {
                Path target = Path.of(appProperties.getUploadDirectory())
                        .resolve(domain).resolve(QuizQuestion.class.getSimpleName().toLowerCase()).resolve("image");

                Path imagePath = FileHelper.saveMultipartFile(target,
                        file.getOriginalFilename() + "_" + optional.get().getId(),
                        file,
                        "png");

                optional.get().setImagePath(imagePath.toString());
            } else {
                log.warn("File is null or empty");
            }
            return quizQuestionRepository.save(optional.get());
        } else {
            throw new ObjectNotFoundException(Quiz.class.getSimpleName() + " with id " + id);
        }
    }

    @Override
    public List<Quiz> getQuizCodesByCategory(String category) {
        return repository().findByCategoryIn(Arrays.asList(category));
    }

    @Override
    public Quiz getQuizByCode(String code) {
        return repository().findByCodeIgnoreCase(code).orElse(null);
    }

    /**
     * Gets upload directory.
     *
     * @return the upload directory
     */
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }
}
