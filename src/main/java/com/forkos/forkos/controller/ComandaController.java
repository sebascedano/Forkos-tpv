package com.forkos.forkos.controller;

import com.forkos.forkos.model.Comanda; // Importa entidades y DTOs (futuros)
import com.forkos.forkos.model.ItemComanda;
// Importa el servicio de Comandas
import com.forkos.forkos.service.ComandaService;

// Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones comunes de web

// Importaciones de Java
import java.util.List;
import java.util.Optional;

// --- Importaciones de Lombok ---
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
// -------------------------------


// --- Clases simples para el cuerpo de las peticiones POST/PUT usando Lombok ---
// Puestas aquí como clases internas static. Si prefieres ponerlas en el paquete DTO,
// cópialas, ponlas en archivos separados en com.forkos.forkos.dto/ y asegúrate de importarlas aquí.

// Request para crear una Comanda: espera { "mesaId": ..., "mozoId": ... }
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CrearComandaRequest {
    private Long mesaId;
    private Long mozoId;
}

// Request para agregar un ítem a una Comanda: espera { "productoId": ..., "cantidad": ..., "notas": ... }
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class AgregarItemRequest {
    private Long productoId;
    private int cantidad;
    private String notas;
}

// Request para actualizar el estado de una Comanda: espera { "nuevoEstado": "..." }
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class UpdateEstadoRequest {
    private String nuevoEstado;
}
// ------------------------------------------------------------------------------


@RestController // Indica a Spring que esta clase es un controlador REST
@RequestMapping("/api/comandas") // Define la ruta base para todos los endpoints en este controlador
public class ComandaController {

    private final ComandaService comandaService; // Inyectamos el servicio de Comandas

    // Inyección de dependencia del servicio a través del constructor
    @Autowired
    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    // Endpoint para crear una nueva comanda
    // POST http://localhost:8080/api/comandas
    // Cuerpo: JSON { "mesaId": 1, "mozoId": 2 }
    @PostMapping
    public ResponseEntity<Object> crearComanda(@RequestBody CrearComandaRequest request) {
        try {
            Comanda nuevaComanda = comandaService.crearComanda(request.getMesaId(), request.getMozoId());
            // En caso de éxito, retornamos ResponseEntity<Comanda>
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComanda); // 201 Created
        } catch (RuntimeException e) { // Captura excepciones del servicio
            // En caso de error, retornamos ResponseEntity<String>
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear comanda: " + e.getMessage());
        }
    }

    // Endpoint para agregar un ítem a una comanda existente
    // POST http://localhost:8080/api/comandas/{comandaId}/items
    // Cuerpo: JSON { "productoId": 3, "cantidad": 2, "notas": "Sin cebolla" }
    @PostMapping("/{comandaId}/items")
    public ResponseEntity<ItemComanda> agregarItemAComanda(@PathVariable Long comandaId,
                                                           @RequestBody AgregarItemRequest request) {
        try {
            ItemComanda nuevoItem = comandaService.agregarItemAComanda(
                    comandaId,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getNotas()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem); // 201 Created
        } catch (RuntimeException e) { // Captura excepciones del servicio (Comanda/Producto no encontrado, cantidad <= 0, etc.)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Endpoint para obtener una comanda por su ID (incluyendo sus ítems)
    // GET http://localhost:8080/api/comandas/{comandaId}
    @GetMapping("/{comandaId}")
    public ResponseEntity<Comanda> getComandaById(@PathVariable Long comandaId) {
        Optional<Comanda> comandaOpt = comandaService.getComandaById(comandaId);
        if (comandaOpt.isPresent()) {
            return ResponseEntity.ok(comandaOpt.get()); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found si la comanda no existe
        }
    }

    // Endpoint para obtener todas las comandas
    // GET http://localhost:8080/api/comandas
    @GetMapping
    public ResponseEntity<List<Comanda>> getAllComandas() {
        List<Comanda> comandas = comandaService.getAllComandas();
        return ResponseEntity.ok(comandas); // 200 OK
    }

    // Endpoint para obtener solo las comandas abiertas
    // GET http://localhost:8080/api/comandas/open
    @GetMapping("/open")
    public ResponseEntity<List<Comanda>> getComandasAbiertas() {
        List<Comanda> comandas = comandaService.getComandasAbiertas();
        return ResponseEntity.ok(comandas); // 200 OK
    }

    // Endpoint para actualizar el estado de una comanda
    // PUT http://localhost:8080/api/comandas/{comandaId}/estado
    // Cuerpo: JSON { "nuevoEstado": "PENDIENTE_PAGO" } (usando los Strings "ABIERTA", "PENDIENTE_PAGO", "CERRADA")
    @PutMapping("/{comandaId}/estado")
    public ResponseEntity<Comanda> updateEstadoComanda(@PathVariable Long comandaId,
                                                       @RequestBody UpdateEstadoRequest request) {
        try {
            Comanda updatedComanda = comandaService.updateEstadoComanda(comandaId, request.getNuevoEstado());
            return ResponseEntity.ok(updatedComanda); // 200 OK
        } catch (RuntimeException e) { // Captura excepciones del servicio (Comanda no encontrada, estado inválido)
            if (e.getMessage().contains("no encontrada")) { // Validación simple basada en mensaje de error
                return ResponseEntity.notFound().build(); // 404 si no existe
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request para estado inválido o transición no permitida
            }
        }
    }

    // Endpoint para cerrar una comanda (marcar como pagada, calcular total)
    // PUT http://localhost:8080/api/comandas/{comandaId}/cerrar
    @PutMapping("/{comandaId}/cerrar")
    public ResponseEntity<Comanda> cerrarComanda(@PathVariable Long comandaId) {
        try {
            Comanda closedComanda = comandaService.cerrarComanda(comandaId);
            return ResponseEntity.ok(closedComanda); // 200 OK
        } catch (RuntimeException e) { // Captura excepciones del servicio (Comanda no encontrada, estado no permitido para cerrar)
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build(); // 404 si no existe
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
            }
        }
    }

    // Endpoint para eliminar un ítem de comanda
    // DELETE http://localhost:8080/api/comandas/items/{itemId}
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> eliminarItemComanda(@PathVariable Long itemId) {
        try {
            comandaService.eliminarItemComanda(itemId);
            return ResponseEntity.noContent().build(); // 204 No Content (Éxito, sin contenido)
        } catch (RuntimeException e) { // Captura excepciones del servicio (Item no encontrado)
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build(); // 404 Not Found
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error para otros fallos
            }
        }
    }

    // Endpoint para eliminar una comanda completa
    // DELETE http://localhost:8080/api/comandas/{comandaId}
    @DeleteMapping("/{comandaId}")
    public ResponseEntity<Void> eliminarComanda(@PathVariable Long comandaId) {
        try {
            comandaService.eliminarComanda(comandaId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) { // Captura excepciones del servicio (Comanda no encontrada)
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build(); // 404 Not Found
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
            }
        }
    }

    // Puedes añadir más endpoints aquí (ej: obtener comandas por mesa, filtrar por fecha, etc.)
}