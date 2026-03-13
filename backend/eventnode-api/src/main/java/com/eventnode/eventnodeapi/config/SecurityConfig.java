package com.eventnode.eventnodeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/alumnos/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/eventos", "/api/eventos/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> 
                        response.sendError(org.springframework.http.HttpStatus.UNAUTHORIZED.value(), 
                                           org.springframework.http.HttpStatus.UNAUTHORIZED.getReasonPhrase())));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Username debe coincidir con usuarios.correo para poder obtener id_usuario dinámicamente
        UserDetails admin = User.withUsername("admin@eventnode.com")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMINISTRADOR")
                .build();

        UserDetails superadmin = User.withUsername("superadmin@eventnode.com")
                .password(passwordEncoder.encode("super123"))
                .roles("SUPERADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, superadmin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

