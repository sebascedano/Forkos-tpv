// --- ComandaController.java (CORREGIDO Y COMPLETO) ---
package com.forkos.forkos.controller;

import com.forkos.forkos.dto.ComandaResponseDTO;
import com.forkos.forkos.dto.ItemComandaResponseDTO;
import com.forkos.forkos.service.ComandaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


// Clases DTO internas para las peticiones (request bodies)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CrearComandaRequest {
    private Long mesaId;
    private Long mozoId;
    private Integer cantidadComensales;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class AgregarItemRequest {
    private Long productoId;
    private int cantidad;
    private String notas;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class UpdateEstadoRequest {
    private String nuevoEstado;
}


@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService comandaService;

    @Autowired
    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    // Endpoint para obtener la comanda ABIERTA de una MESA específica
    // GET http://localhost:8080/api/comandas/mesa/{mesaId}/abierta
    @GetMapping("/mesa/{mesaId}/abierta")
    public ResponseEntity<ComandaResponseDTO> getComandaAbiertaPorMesaId(@PathVariable Long mesaId) {
        Optional<ComandaResponseDTO> comandaDTO = comandaService.getComandaAbiertaPorMesaId(mesaId);
        return comandaDTO
                .map(ResponseEntity::ok) // Si se encuentra, devuelve 200 OK con la comanda
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }


    @PostMapping
    public ResponseEntity<Object> crearComanda(@RequestBody CrearComandaRequest request) {
        try {
            ComandaResponseDTO nuevaComanda = comandaService.crearComanda(
                    request.getMesaId(),
                    request.getMozoId(),
                    request.getCantidadComensales());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComanda);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear comanda: " + e.getMessage());
        }
    }

    @PostMapping("/{comandaId}/items")
    public ResponseEntity<ItemComandaResponseDTO> agregarItemAComanda(@PathVariable Long comandaId,
                                                                      @RequestBody AgregarItemRequest request) {
        try {
            ItemComandaResponseDTO nuevoItem = comandaService.agregarItemAComanda(
                    comandaId,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getNotas()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{comandaId}")
    public ResponseEntity<ComandaResponseDTO> getComandaById(@PathVariable Long comandaId) {
        Optional<ComandaResponseDTO> comandaOpt = comandaService.getComandaById(comandaId);
        if (comandaOpt.isPresent()) {
            return ResponseEntity.ok(comandaOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/open")
    public ResponseEntity<List<ComandaResponseDTO>> getComandasAbiertas() {
        List<ComandaResponseDTO> comandas = comandaService.getComandasAbiertas();
        return ResponseEntity.ok(comandas);
    }

    @PutMapping("/{comandaId}/estado")
    public ResponseEntity<ComandaResponseDTO> updateEstadoComanda(@PathVariable Long comandaId,
                                                                  @RequestBody UpdateEstadoRequest request) {
        try {
            ComandaResponseDTO updatedComanda = comandaService.updateEstadoComanda(comandaId, request.getNuevoEstado());
            return ResponseEntity.ok(updatedComanda);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }

    @PutMapping("/{comandaId}/cerrar")
    public ResponseEntity<ComandaResponseDTO> cerrarComanda(@PathVariable Long comandaId) {
        try {
            ComandaResponseDTO closedComanda = comandaService.cerrarComanda(comandaId);
            return ResponseEntity.ok(closedComanda);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> eliminarItemComanda(@PathVariable Long itemId) {
        try {
            comandaService.eliminarItemComanda(itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @DeleteMapping("/{comandaId}")
    public ResponseEntity<Void> eliminarComanda(@PathVariable Long comandaId) {
        try {
            comandaService.eliminarComanda(comandaId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}