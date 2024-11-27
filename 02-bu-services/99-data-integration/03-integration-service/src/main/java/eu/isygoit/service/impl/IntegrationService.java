package eu.isygoit.service.impl;

import eu.isygoit.model.IntegrationOrder;
import eu.isygoit.service.IIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * The type Integration service.
 */
@Slf4j
@Service
public class IntegrationService implements IIntegrationService<String> {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String create(IntegrationOrder order, String bodyJson) {
        //String url = "http://identity-service/api/private/account";
        String url = new StringBuilder(order.getServiceName()).append("/").append(order.getMapping()).toString();
        try {
            RequestEntity<String> requestEntity = RequestEntity.post(new URL(url).toURI()).contentType(MediaType.APPLICATION_JSON).body(bodyJson);
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            return response.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String update(IntegrationOrder order, String bodyJson) {
        //String url = "http://identity-service/api/private/account";
        String url = new StringBuilder(order.getServiceName()).append("/").append(order.getMapping()).toString();
        try {
            RequestEntity<String> requestEntity = RequestEntity.put(new URL(url).toURI()).contentType(MediaType.APPLICATION_JSON).body(bodyJson);
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            return response.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(IntegrationOrder order, String identifier) {
        //String url = "http://identity-service/api/private/account";
        String url = new StringBuilder(order.getServiceName()).append("/").append(order.getMapping()).append("/").append(identifier).toString();
        restTemplate.delete(url);
        return true;
    }

    @Override
    public List<String> fetch(IntegrationOrder order) {
        //String url = "http://identity-service/api/private/account";
        String url = new StringBuilder(order.getServiceName()).append("/").append(order.getMapping()).toString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
