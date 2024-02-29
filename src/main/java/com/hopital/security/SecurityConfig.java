package com.hopital.security;

import com.hopital.security.repositories.AppRoleRepository;
import com.hopital.security.services.AccountService;
import com.hopital.security.services.UserDetailsServiceImpl;
import com.mysql.cj.MysqlType;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // si on ne veut pas utiliser un password encoder ov va insérer avant le password {noop}

    // La startégie InMemoryUserDetailsManagement

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("123")).roles("user").build(),
                User.withUsername("user2").password(passwordEncoder.encode("123")).roles("user").build(),
                User.withUsername("admin").password(passwordEncoder.encode("123")).roles("user", "admin").build());
        return inMemoryUserDetailsManager;
    }

    // La startégie JdbcUserDetailsManager

    //@Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("patients-users-db")
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }

    @Bean // execute la méthode au démarrage du l'application
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //.formLogin(Customizer.withDefaults())
                .formLogin(fl -> fl
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/webjars/**", "/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .requestMatchers("/user/**").hasRole("user")
                        .anyRequest().authenticated())
                //.rememberMe(Customizer.withDefaults())
                .exceptionHandling(exHandl -> exHandl
                        .accessDeniedPage("/notAuthorized"))
                .userDetailsService(userDetailsService);
        return httpSecurity.build();
    }

    //@Bean
    CommandLineRunner commandLineRunnerJdbcUsers(JdbcUserDetailsManager jdbcUserDetailsManager) {
        return args -> {
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user1").password(passwordEncoder.encode("123")).roles("user").build()
            );
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin").password(passwordEncoder.encode("123")).roles("user", "admin").build()
            );
        };
    }

    //@Bean
    CommandLineRunner commandLineRunnerUserDetailsService(AccountService accountService) {
        return args -> {
            accountService.addRole("admin");
            accountService.addRole("user");
            //
            accountService.addUser("user", "123", "user@gmail.com", "123");
            accountService.addUser("admin", "123", "admin@gmail.com", "123");
            //
            accountService.addRoleToUser("user", "user");
            accountService.addRoleToUser("admin", "user");
            accountService.addRoleToUser("admin", "admin");
        };
    }

}