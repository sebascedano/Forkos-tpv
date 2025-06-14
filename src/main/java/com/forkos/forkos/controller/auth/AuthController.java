package com.forkos.forkos.controller.auth;

import com.forkos.forkos.dto.AuthResponseDTO;
import com.forkos.forkos.dto.request.LoginRequest;
import com.forkos.forkos.dto.request.SignupRequest;
import com.forkos.forkos.model.Rol;
import com.forkos.forkos.model.Usuario;
import com.forkos.forkos.repository.RolRepository;
import com.forkos.forkos.repository.UsuarioRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = "prueba123";
            return ResponseEntity.ok(new AuthResponseDTO(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: Usuario o contraseña incorrectos.");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: Usuario no encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno durante la autenticación: " + ex.getMessage());
        }
    }

    @GetMapping("/me")
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
                userData.put("rol", role);
                return ResponseEntity.ok(userData);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo obtener la información del usuario.");
    }

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private RolRepository rolRepository; // si tienes repo de roles

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            // Verificar username y email únicos
            if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe.");
            }
            if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está registrado.");
            }

            // Encriptar la contraseña
            String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());

            // Crear el usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(signupRequest.getNombre());
            nuevoUsuario.setUsername(signupRequest.getUsername());
            nuevoUsuario.setEmail(signupRequest.getEmail());
            nuevoUsuario.setPasswordHash(hashedPassword);
            nuevoUsuario.setActivo(true);

            // Asigna el rol por defecto (ajusta el nombre a lo que tengas)
            Rol rolPorDefecto = rolRepository.findByNombre("MOZO")
                    .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado."));
            nuevoUsuario.setRol(rolPorDefecto);

            userRepository.save(nuevoUsuario);

            return ResponseEntity.ok("Usuario registrado correctamente.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + ex.getMessage());
        }
    }

}

