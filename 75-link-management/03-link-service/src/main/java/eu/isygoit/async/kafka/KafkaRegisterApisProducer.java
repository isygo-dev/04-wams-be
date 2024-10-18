package eu.isygoit.async.kafka;

import eu.isygoit.dto.extendable.ApiPermissionModelDto;
import eu.isygoit.helper.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The type Kafka register apis producer.
 */
@Slf4j
@Service
public class KafkaRegisterApisProducer {

    @Value("${spring.kafka.topics.register-api-permission}")
    private String register_api_permission_topic;

    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Send message.
     *
     * @param message the message
     * @throws IOException the io exception
     */
    public void sendMessage(ApiPermissionModelDto message) throws IOException {
        log.info("Message sent to {} -> {}", register_api_permission_topic, message);
        kafkaTemplate.send(register_api_permission_topic, JsonHelper.toJson(message));
    }
}
