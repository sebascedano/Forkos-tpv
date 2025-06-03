package com.forkos.forkos.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Registrar el módulo de Hibernate para manejar las relaciones JPA y lazy loading
        mapper.registerModule(new Hibernate6Module());

        // 2. Registrar el módulo para tipos de fecha y hora de Java 8 (LocalDateTime, etc.)
        // ESTO ES LO QUE FALTA EN TU CONFIGURACIÓN ACTUAL Y CAUSA EL ERROR
        mapper.registerModule(new JavaTimeModule());

        // Opcional: Deshabilitar la escritura de fechas como timestamps.
        // Esto hace que LocalDateTime se serialice como un string ISO 8601 (ej: "2025-05-29T23:28:12.625")
        // en lugar de un número. Es una buena práctica para APIs REST.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    /*
    // Opcional: Si quieres mantener el bean de HibernateModule separado, podrías hacerlo así,
    // pero la forma combinada en objectMapper() es más común y directa.
    // Si tienes solo este bean, Spring Boot creará otro ObjectMapper por defecto sin JavaTimeModule.
    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
    }
    */
}