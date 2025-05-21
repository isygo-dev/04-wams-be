package eu.isygoit.service.impl;

import eu.isygoit.api.AbstractApiExtractor;
import eu.isygoit.async.kafka.KafkaRegisterApisProducer;
import eu.isygoit.mapper.ApiPermissionMapper;
import eu.isygoit.model.ApiPermission;
import eu.isygoit.repository.ApiPermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional
public class ApiExtractorService extends AbstractApiExtractor<ApiPermission> {

    private final KafkaRegisterApisProducer kafkaRegisterApisProducer;
    private final ApiPermissionRepository apiPermissionRepository;
    private final ApiPermissionMapper apiPermissionMapper;
    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public ApiExtractorService(KafkaRegisterApisProducer kafkaRegisterApisProducer, ApiPermissionRepository apiPermissionRepository, ApiPermissionMapper apiPermissionMapper) {
        this.kafkaRegisterApisProducer = kafkaRegisterApisProducer;
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionMapper = apiPermissionMapper;
    }

    @Transactional
    @Override
    public ApiPermission saveApi(ApiPermission api) {
        // Retrieve or save the API permission
        ApiPermission savedApi = apiPermissionRepository.findByServiceNameAndObjectAndMethodAndRqTypeAndPath(
                        api.getServiceName(),
                        api.getObject(),
                        api.getMethod(),
                        api.getRqType(),
                        api.getPath())
                .orElseGet(() -> apiPermissionRepository.save(api));  // Save only if not found

        // Send the message to Kafka
        try {
            kafkaRegisterApisProducer.sendMessage(apiPermissionMapper.entityToDto(savedApi));
        } catch (IOException e) {
            log.error("<Error>: Register API permission via Kafka topic failed with error: {} ", e);
        }

        return savedApi;
    }

    @Override
    public ApiPermission newInstance() {
        return ApiPermission.builder().serviceName(serviceName).build();
    }
}
