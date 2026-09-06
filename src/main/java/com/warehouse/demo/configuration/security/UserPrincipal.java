package com.warehouse.demo.configuration.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.warehouse.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + user.getEmployee().getPosition().getCodeName();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        Collection<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(authority);

        return authorityList;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmployee().getEmployeeNumber();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean hasAnyRole(String... roleCodes) {
        return Arrays.stream(roleCodes)
            .anyMatch(rc -> this.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rc))
        );
    }
}
