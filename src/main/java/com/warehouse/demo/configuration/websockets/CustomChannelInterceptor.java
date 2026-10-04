package com.warehouse.demo.configuration.websockets;

import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import com.warehouse.demo.configuration.security.keycloak.KeycloakJwtConverter;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final JwtDecoder jwtDecoder;

    @Override
    public @Nullable Message<?> preSend(
        Message<?> message, 
        MessageChannel channel
    ) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String header = accessor.getFirstNativeHeader("Authorization");
            if (
                header == null 
                || !header.startsWith("Bearer ")
            ) throw new AccessDeniedException("Invalid header. Must contain JWT when connecting.");

            String stringToken = header.substring("Bearer ".length()); // Skip "Bearer "
            Jwt jwt = jwtDecoder.decode(stringToken);
            AbstractAuthenticationToken authenticationToken = new KeycloakJwtConverter().convert(jwt);
            accessor.setUser(authenticationToken);
        }

        return message;
    }
}
