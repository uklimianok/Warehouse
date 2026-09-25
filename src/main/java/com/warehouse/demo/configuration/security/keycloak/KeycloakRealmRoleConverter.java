package com.warehouse.demo.configuration.security.keycloak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {  // Converter already has @Bean
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> claimsMap = source.getClaimAsMap("realm_access");
        if (claimsMap == null || claimsMap.isEmpty()) return List.of();

        Object rolesObject = claimsMap.get("roles");
        if (!(rolesObject instanceof Collection<?> roles)) return List.of();    // Such "if" syntax works from Java 21
        
        Collection<GrantedAuthority> rolesCollection = new ArrayList<>();
        for (Object role : roles) {
            rolesCollection.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return rolesCollection;
    }
}
