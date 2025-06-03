package com.forkos.forkos.service.impl; // O tu paquete de seguridad

// --- Importaciones de Spring Security ---
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Para los roles/permisos
// ---------------------------------------

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Para que Spring lo detecte
import org.springframework.transaction.annotation.Transactional; // Para asegurar que la carga de usuario esté dentro de una transacción (si es necesario)

// --- Importaciones de tu proyecto ---
import com.forkos.forkos.model.Usuario; // Importa tu entidad Usuario
import com.forkos.forkos.repository.UsuarioRepository; // Importa tu repositorio de Usuario
// ------------------------------------

import java.util.Collections; // Para crear colecciones de permisos
import java.util.Optional; // Importar Optional si tu repositorio retorna Optional


@Service // Indica que esta clase es un componente de servicio gestionado por Spring
public class CustomUserDetailsService implements UserDetailsService { // Implementa la interfaz UserDetailsService

    private final UsuarioRepository usuarioRepository; // Inyectamos el repositorio de Usuario

    @Autowired // Constructor para inyección de dependencia del repositorio
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // === Método principal que Spring Security llamará para cargar el usuario ===
    @Override
    @Transactional(readOnly = true) // Generalmente es de solo lectura al cargar un usuario
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscar el usuario en tu base de datos por el nombre de usuario
        // Usamos el método findByUsername que añadiremos al repositorio.
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username); // <-- Llama a tu repositorio

        Usuario usuario = usuarioOpt.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username)); // Si no se encuentra, lanza esta excepción estándar de Spring Security

        // 2. Convertir tu entidad Usuario a un objeto UserDetails de Spring Security
        // Usamos la implementación org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),           // El nombre de usuario (de tu entidad)
                usuario.getPasswordHash(),      // La contraseña (hasheada, de tu entidad)
                usuario.isActivo(),             // Si el usuario está activo (campo 'activo' de tu entidad)
                true,                   // La cuenta no está expirada (generalmente true)
                true,                   // Las credenciales no están expiradas (generalmente true)
                true,                   // La cuenta no está bloqueada (generalmente true)
                // 3. Convertir el/los rol/es del usuario a una colección de GrantedAuthority
                // Spring Security usa GrantedAuthority para representar permisos/roles
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre())) // Asumiendo que Rol tiene getNombre()
        );
    }
}