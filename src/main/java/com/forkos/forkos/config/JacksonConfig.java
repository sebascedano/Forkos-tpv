package com.forkos.forkos.config;

import com.fasterxml.jackson.databind.Module; // Importa Module
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module; // Importa Hibernate6Module
import org.springframework.context.annotation.Bean; // Importa Bean
import org.springframework.context.annotation.Configuration; // Importa Configuration

@Configuration // Indica a Spring que esta es una clase de configuración
public class JacksonConfig {

    @Bean // Define un bean gestionado por Spring
    public Module hibernateModule() {
        // Retorna una instancia del módulo de Hibernate para Jackson
        // Puedes configurarlo si es necesario, pero la configuración por defecto suele ser suficiente
        return new Hibernate6Module();
    }
}