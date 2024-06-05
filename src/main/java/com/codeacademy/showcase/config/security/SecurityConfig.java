package com.codeacademy.showcase.config.security;

import com.codeacademy.showcase.utilenum.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/operator/**").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_OPERATOR.name())
                                .requestMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers("/user").permitAll()
                                .requestMatchers("/user/create").permitAll()
                                .requestMatchers("/error/**").permitAll() //spring seems to redirect to /error when submit invalid type, so this endpoint need to be permitted
                                .anyRequest().authenticated()

                )
                //better use for token (JWT, OAUTH2) scenarios
//                .sessionManagement((session) ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
//                .sessionManagement((session) ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic((httpBasic) -> httpBasic.realmName("Restaurant Realm"));

        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

}
