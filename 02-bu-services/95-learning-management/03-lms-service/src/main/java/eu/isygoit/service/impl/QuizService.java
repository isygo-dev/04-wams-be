package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
import eu.isygoit.model.Quiz;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.QuizRepository;
import eu.isygoit.service.IQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Quiz service.
 */
@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = QuizRepository.class)
public class QuizService extends CodeAssignableService<Long, Quiz, QuizRepository> implements IQuizService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(Quiz.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("QIZ")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }
}
