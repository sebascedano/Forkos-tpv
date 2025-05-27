package com.forkos.forkos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean; // <-- Asegúrate de importar Bean
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Asegúrate de importar PasswordEncoder

@SpringBootApplication
public class ForkOsApplication {

	public static void main(String[] args) {

		SpringApplication.run(ForkOsApplication.class, args);
	}


}
