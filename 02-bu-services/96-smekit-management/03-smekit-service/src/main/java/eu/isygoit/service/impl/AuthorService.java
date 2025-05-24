package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.FileImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Author;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.AuthorRepository;
import eu.isygoit.service.IAutherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = AuthorRepository.class)
@DmsLinkFileService(DmsLinkedFileService.class)
public class AuthorService extends FileImageService<Long, Author, AuthorRepository> implements IAutherService {
    private final AppProperties appProperties;
    private final AuthorRepository authorRepository;

    public AuthorService(AppProperties appProperties, AuthorRepository authorRepository) {
        this.appProperties = appProperties;
        this.authorRepository = authorRepository;
    }

    @Override
    public String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }


    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Author.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("AUTH")
                .valueLength(6L)
                .value(1L)
                .build();
    }

    public double getMonthlyTrend(LocalDate referenceDate) {
        Date startOfMonth = convertToDate(referenceDate.withDayOfMonth(1).atStartOfDay());
        Date startOfLastMonth = convertToDate(referenceDate.withDayOfMonth(1).minusMonths(1).atStartOfDay());
        Date endOfLastMonth = convertToDate(referenceDate.withDayOfMonth(1).atStartOfDay().minusSeconds(1));

        long currentMonthCount = authorRepository.countByCreateDateBetween(
                startOfMonth,
                new Date()
        );

        long lastMonthCount = authorRepository.countByCreateDateBetween(
                startOfLastMonth,
                endOfLastMonth
        );

        return calculateTrend(currentMonthCount, lastMonthCount);
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private double calculateTrend(long currentCount, long previousCount) {
        if (previousCount == 0) {
            return currentCount > 0 ? 100.0 : 0.0;
        }
        return ((double) (currentCount - previousCount) / previousCount) * 100;
    }
}
