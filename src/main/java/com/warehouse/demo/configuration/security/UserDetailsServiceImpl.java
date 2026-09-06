package com.warehouse.demo.configuration.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.entity.user.User;
import com.warehouse.demo.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmployee_EmployeeNumber(username);
        if (user.isPresent()) return new UserPrincipal(user.get());
        else throw new UsernameNotFoundException("Invalid number.");
    }
}
