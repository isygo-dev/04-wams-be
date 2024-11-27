package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.IntegrationOrderFileDto;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.mapper.IntegrationOrderFileMapper;
import eu.isygoit.model.IntegrationOrder;
import eu.isygoit.service.impl.IntegrationOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * The type Integration order controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationOrderFileMapper.class, minMapper = IntegrationOrderFileMapper.class, service = IntegrationOrderService.class)
@RequestMapping(value = "/api/v1/private/integration/order")
public class IntegrationOrderController extends MappedCrudController<Long, IntegrationOrder, IntegrationOrderFileDto,
        IntegrationOrderFileDto, IntegrationOrderService> {

    private WebClient webClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Instantiates a new Integration order controller.
     *
     * @param webClientBuilder the web client builder
     */
    @Autowired
    public IntegrationOrderController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Call rest template response entity.
     *
     * @return the response entity
     */
    @GetMapping(path = "/call_rest_templateGet")
    public ResponseEntity<List> callRestTemplate() {
        String getAccountsURI = "http://identity-service/api/private/account";
        ResponseEntity<List> response = restTemplate.getForEntity(getAccountsURI, List.class);
        return response;
    }

    /**
     * Call rest template post response entity.
     *
     * @return the response entity
     */
    @GetMapping(path = "/call_rest_template_post")
    public ResponseEntity<String> callRestTemplatePost() {
        String getAccountsURI = "http://identity-service/api/private/account";
        String postBodyJson = "{\n" +
                "    \"email\": \"s.mbarki@isygoit.eu\",\n" +
                "    \"fullName\": \"Sami Mbarki\",\n" +
                "    \"origin\": \"SYS_ADMIN\",\n" +
                "    \"domain\": \"isygoit.eu\",\n" +
                "    \"language\": \"EN\",\n" +
                "    \"adminStatus\": \"ENABLED\",\n" +
                "    \"systemStatus\": \"IDLE\",\n" +
                "    \"accountDetails\": {\n" +
                "      \"firstName\": \"Sami\",\n" +
                "      \"lastName\": \"Mbarki\",\n" +
                "      \"country\": null,\n" +
                "      \"contacts\": [],\n" +
                "      \"address\": {\n" +
                "        \"country\": \"\",\n" +
                "        \"state\": \"\",\n" +
                "        \"city\": \"\",\n" +
                "        \"street\": \"\",\n" +
                "        \"zipCode\": \"0\",\n" +
                "        \"additionalInfo\": \"\",\n" +
                "        \"latitude\": null,\n" +
                "        \"longitude\": null,\n" +
                "        \"compAddress\": []\n" +
                "      }\n" +
                "    },\n" +
                "    \"roleInfo\": [],\n" +
                "    \"functionRole\": \"Domain Admin\",\n" +
                "    \"imagePath\": \"\\\\uploads\\\\isygoit.eu\\\\account\\\\image\\\\blob_ACT000003.png\",\n" +
                "    \"phoneNumber\": \"+21658965478\",\n" +
                "    \"isAdmin\": true,\n" +
                "    \"authType\": \"OTP\",\n" +
                "    \"accountType\": \"domain-user\"\n" +
                "  }";

        RequestEntity<String> requestEntity = null;
        try {
            requestEntity = RequestEntity.post(new URL(getAccountsURI).toURI()).contentType(MediaType.APPLICATION_JSON).body(postBodyJson);
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            return response;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Call rest web client response entity.
     *
     * @return the response entity
     */
    @GetMapping(path = "/call_rest_web_client")
    public ResponseEntity<String> callRestWebClient() {

        String authServiceUrl = "http://identity-service";
/*        return this.webClient
                .get()
                .uri(authServiceUrl + "/api/v1/private/account")
                .retrieve().toEntity(String.class).block();*/

        return this.webClient
                .post()
                .uri(authServiceUrl + "/api/v1/private/account")
                .body("{\n" +
                        "    \"email\": \"s.mbarki@isygoit.eu\",\n" +
                        "    \"fullName\": \"Sami Mbarki\",\n" +
                        "    \"origin\": \"SYS_ADMIN\",\n" +
                        "    \"domain\": \"isygoit.eu\",\n" +
                        "    \"language\": \"EN\",\n" +
                        "    \"adminStatus\": \"ENABLED\",\n" +
                        "    \"systemStatus\": \"IDLE\",\n" +
                        "    \"accountDetails\": {\n" +
                        "      \"firstName\": \"Sami\",\n" +
                        "      \"lastName\": \"Mbarki\",\n" +
                        "      \"country\": null,\n" +
                        "      \"contacts\": [],\n" +
                        "      \"address\": {\n" +
                        "        \"country\": \"\",\n" +
                        "        \"state\": \"\",\n" +
                        "        \"city\": \"\",\n" +
                        "        \"street\": \"\",\n" +
                        "        \"zipCode\": \"0\",\n" +
                        "        \"additionalInfo\": \"\",\n" +
                        "        \"latitude\": null,\n" +
                        "        \"longitude\": null,\n" +
                        "        \"compAddress\": []\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"roleInfo\": [],\n" +
                        "    \"functionRole\": \"Domain Admin\",\n" +
                        "    \"imagePath\": \"\\\\uploads\\\\isygoit.eu\\\\account\\\\image\\\\blob_ACT000003.png\",\n" +
                        "    \"phoneNumber\": \"+21658965478\",\n" +
                        "    \"isAdmin\": true,\n" +
                        "    \"authType\": \"OTP\",\n" +
                        "    \"accountType\": \"domain-user\"\n" +
                        "  }", String.class)
                .retrieve().toEntity(String.class).block();
    }
}
