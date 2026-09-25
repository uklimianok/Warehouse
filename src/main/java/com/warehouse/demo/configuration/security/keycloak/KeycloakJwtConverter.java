package com.warehouse.demo.configuration.security.keycloak;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.warehouse.demo.configuration.security.UserPrincipal;

public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = new KeycloakRealmRoleConverter().convert(source);
        String employeeNumber = source.getClaimAsString("preferred_username");
        UserPrincipal userPrincipal = new UserPrincipal(employeeNumber, authorities);

        return UsernamePasswordAuthenticationToken.authenticated(userPrincipal, source, authorities);
    }
}
