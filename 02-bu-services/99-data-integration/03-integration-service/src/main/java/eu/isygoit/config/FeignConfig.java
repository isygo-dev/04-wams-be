package eu.isygoit.config;

import eu.isygoit.jwt.JwtService;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Feign config.
 */
@Configuration
public class FeignConfig {

    /**
     * Not filtered request interceptor request interceptor.
     *
     * @return the request interceptor
     */
    @Bean
    public RequestInterceptor notFilteredRequestInterceptor() {
        return requestTemplate -> {
            //requestTemplate.header("SHOULD_NOT_FILTER_KEY", AbstractJwtAuthFilter.SHOULD_NOT_FILTER_KEY);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return;
            }
            HttpServletRequest request = requestAttributes.getRequest();
            if (request == null) {
                return;
            }
            String jwtToken = request.getHeader(JwtService.AUTHORIZATION);
            if (jwtToken == null) {
                return;
            }

            requestTemplate.header(JwtService.AUTHORIZATION, jwtToken);
        };
    }

    /**
     * Load balanced web client builder web client . builder.
     *
     * @return the web client . builder
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Rest template rest template.
     *
     * @param builder the builder
     * @return the rest template
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}