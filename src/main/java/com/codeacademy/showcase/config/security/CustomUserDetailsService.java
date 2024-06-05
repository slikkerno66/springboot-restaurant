package com.codeacademy.showcase.config.security;

import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.repository.UserRepository;
import com.codeacademy.showcase.utils.UserAuthorityUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession httpSession;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        httpSession.invalidate();

        return User.withUsername(users.getUsername())
                .password(users.getPassword())
                .authorities(UserAuthorityUtils.getRoles(users.getRole()))
                .build();
    }

}
