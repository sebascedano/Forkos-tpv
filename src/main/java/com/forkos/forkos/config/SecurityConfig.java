package com.forkos.forkos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Para deshabilitar CSRF
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importar BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder interface
import org.springframework.security.web.SecurityFilterChain;

// Importaciones necesarias
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (esencial para APIs REST sin sesiones basadas en cookies)
                .csrf(AbstractHttpConfigurer::disable)

                // --- Configurar Reglas de Autorización ---
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        // Permitir acceso público al endpoint de login (lo crearemos en el Paso 5)
                                        .requestMatchers("/api/auth/login").permitAll() // <<<--- PERMITIR EL LOGIN

                                        // Permitir acceso a swagger/openapi si lo usas
                                        .requestMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
                                                "/swagger-resources", "/swagger-resources/**",
                                                "/configuration/ui", "/configuration/security",
                                                "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()

                                        // OTRAS RUTAS PUBLICAS SI LAS TIENES (ej: /api/public/**)
                                        // .requestMatchers("/api/public/**").permitAll()

                                        // Todas las demás peticiones a /api/** REQUIEREN AUTENTICACIÓN
                                        // Esto protege tus endpoints de Comandas, Productos, etc.
                                        .requestMatchers("/api/**").authenticated() // <<<--- REQUIERE AUTENTICACIÓN para /api/...
                        // O si quieres proteger TODO lo que no sea /api/auth/login:
                        // .anyRequest().authenticated()
                )

                // Deshabilitar los mecanismos de autenticación por defecto de Spring
                // Usaremos un enfoque basado en API (login por POST a /api/auth/login)
                .httpBasic(AbstractHttpConfigurer::disable) // Deshabilitar HTTP Basic
                .formLogin(AbstractHttpConfigurer::disable); // Deshabilitar Login de Formulario

        return http.build();
    }

    // === Añade este Bean para el PasswordEncoder (si no lo tienes ya) ===
    // Asegúrate de que este bean exista
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ==============================================

    // === Configurar el AuthenticationManager ===
    // Spring Boot 2.x/3.x recomienda obtener el AuthenticationManager así
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // === Configurar el AuthenticationProvider ===
    // Este proveedor usará tu UserDetailsService y PasswordEncoder
    // Spring Security lo detectará y lo añadirá al AuthenticationManager por defecto
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Usa tu CustomUserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder); // Usa tu BCryptPasswordEncoder
        return authProvider;
    }


}