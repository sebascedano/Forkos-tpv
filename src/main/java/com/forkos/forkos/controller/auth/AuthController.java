package com.forkos.forkos.controller.auth;

import com.forkos.forkos.dto.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Añadir esta importación

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("Login exitoso para el usuario: " + authentication.getName());

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: Usuario o contraseña incorrectos.");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: Usuario no encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno durante la autenticación: " + ex.getMessage());
        }
    }

    @GetMapping("/me") // <--- ESTE ES EL NUEVO MÉTODO
    public ResponseEntity<?> getUserMe(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay usuario autenticado.");
        }

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
            Object userObject = authToken.getPrincipal();

            if (userObject instanceof User) {
                User userDetails = (User) userObject;
                String username = userDetails.getUsername();
                String role = userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .filter(authority -> authority.startsWith("ROLE_"))
                        .findFirst()
                        .map(authority -> authority.substring("ROLE_".length()))
                        .orElse("UNKNOWN");

                Map<String, String> userData = new HashMap<>();
                userData.put("username", username);
                userData.put("rol", role); // <--- DEBE SER "rol" para que JavaFX lo encuentre
                return ResponseEntity.ok(userData);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo obtener la información del usuario.");
    }
}

