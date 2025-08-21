package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.LoginModel;
import com.portfolio.pushpendra.admin.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private LoginRepo userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginModel user = userDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found "+username));

        // Print fetched username and role for debugging
        System.out.println("Fetched user: " + user.getUsername());
        System.out.println("Fetched role: " + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true, // if you have enabled column
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );

    }
}
