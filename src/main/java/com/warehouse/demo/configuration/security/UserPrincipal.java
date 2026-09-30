package com.warehouse.demo.configuration.security;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserPrincipal implements Principal {
    private final String employeeNumber;
    private final Collection<GrantedAuthority> authorities;

    @Override
    public String getName() {
        return employeeNumber;
    }

    public String getMainRole() {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().matches("/[A-Z_]+/"))  // Matches to something like "TRUCK_DRIVER"
                return authority.getAuthority();
        }

        return "";
    }

    public boolean hasAnyRole(String... roleCodes) {
        return Arrays.stream(roleCodes)
            .anyMatch(rc -> this.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rc))
        );
    }
}
