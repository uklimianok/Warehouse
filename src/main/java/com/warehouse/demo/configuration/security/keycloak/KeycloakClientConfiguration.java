package com.warehouse.demo.configuration.security.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration  
public class KeycloakClientConfiguration {
    @Value("${warehouse.keycloak.admin-url}")
    private String baseUrl;

    @Bean 
    RestClient keycloakRestClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        OAuth2ClientHttpRequestInterceptor requestInterceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        requestInterceptor.setClientRegistrationIdResolver(request -> "keycloak");

        return RestClient
            .builder()
            .baseUrl(baseUrl)
            .requestInterceptor(requestInterceptor)
            .build();
    }
}
