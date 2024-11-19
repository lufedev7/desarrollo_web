package com.proyect.web.configuration;

import com.proyect.web.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth
                            // Rutas públicas
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/products/page").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/products/search").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/products/category/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/products/{id}/related").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                            // Ruta específica de perfil de usuario - MOVER ESTA LÍNEA ARRIBA
                            .requestMatchers(HttpMethod.GET, "/api/users/my-profile").hasAnyRole("USER", "ADMIN")

                            // Rutas de administrador
                            .requestMatchers("/api/users/**").hasRole("ADMIN")
                            .requestMatchers("/api/categories/new").hasRole("ADMIN")
                            .requestMatchers("/api/categories/delete/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                            // Rutas para usuarios vendedores
                            .requestMatchers("/api/products/new").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/products/update/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/products/user/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/products/my-products").hasAnyRole("USER", "ADMIN")

                            // Patch de seller status solo para admin
                            .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/seller").hasRole("USER")

                            // Cualquier otra ruta requiere autenticación
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}