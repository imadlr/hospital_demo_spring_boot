package com.hopital.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // si on ne veut pas utiliser un password encoder ov va insérer avant le password {noop}
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("123")).roles("user").build(),
                User.withUsername("user2").password(passwordEncoder.encode("123")).roles("user").build(),
                User.withUsername("admin").password(passwordEncoder.encode("123")).roles("user", "admin").build());
        return inMemoryUserDetailsManager;
    }

    @Bean // execute la méthode au démarrage du l'application
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**").hasRole("admin")
                        .requestMatchers("/user/**").hasRole("user")
                        .anyRequest().authenticated())
                .exceptionHandling(exHandl -> exHandl
                        .accessDeniedPage("/notAuthorized"));
        return httpSecurity.build();
    }
}