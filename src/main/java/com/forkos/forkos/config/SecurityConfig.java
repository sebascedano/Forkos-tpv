package com.forkos.forkos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Para deshabilitar CSRF
import org.springframework.security.web.SecurityFilterChain;

// Importaciones necesarias
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Indica a Spring que esta es una clase de configuración
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring
public class SecurityConfig {

    @Bean // Declara un bean gestionado por Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (esencial para APIs REST sin sesiones basadas en cookies)
                .csrf(AbstractHttpConfigurer::disable)
                // Configurar reglas de autorización para peticiones HTTP
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permitir acceso a cualquier petición que empiece con /api/productos/
                                .requestMatchers("/api/productos/**").permitAll()
                                .requestMatchers("/api/comandas/**").permitAll()
                                // Puedes añadir otras rutas permitidas aquí si las necesitas (ej: /api/auth/**)
                                // .requestMatchers("/api/auth/**").permitAll()
                                // Cualquier otra petición (a otras rutas) requiere autenticación por ahora
                                .anyRequest().authenticated()
                )
                // Configurar la autenticación HTTP básica por defecto (puedes cambiarla después)
                .httpBasic(withDefaults()); // O .httpBasic() con personalización


        // Retornar la cadena de filtros de seguridad configurada
        return http.build();
    }

    // Más adelante aquí configurarás:
    // - PasswordEncoder (para hashear contraseñas)
    // - AuthenticationManager (para gestionar la autenticación)
    // - UserDetailsService (para cargar detalles de usuario de tu DB)
    // - JWT filters, si usas autenticación basada en tokens
}
