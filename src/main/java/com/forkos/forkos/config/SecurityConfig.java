package com.forkos.forkos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Para deshabilitar CSRF
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importar BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder interface
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ==============================================
    // === Configurar el SecurityFilterChain ===
    // Este método configura las reglas de seguridad para las peticiones HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
                                        "/swagger-resources", "/swagger-resources/**",
                                        "/configuration/ui", "/configuration/security",
                                        "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()

                                // Permite el acceso a /api/user/me a cualquier usuario autenticado
                                .requestMatchers("/api/user/me").authenticated()
                                // Permite el acceso a /api/user/roles a cualquier usuario autenticado
                                .anyRequest().authenticated() // Cualquier otra petición requiere autenticación
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // ==============================================
    // === Configurar el UserDetailsService y PasswordEncoder ===
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ==============================================

    // === Configurar el AuthenticationManager ===
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // === Configurar el AuthenticationProvider ===
    // Este método configura el DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Usa tu CustomUserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder); // Usa tu BCryptPasswordEncoder
        return authProvider;
    }

    // ==============================================
    // === Configurar CORS para permitir peticiones desde el frontend ===
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5174","http://localhost:5173") // Puerto del frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }


}