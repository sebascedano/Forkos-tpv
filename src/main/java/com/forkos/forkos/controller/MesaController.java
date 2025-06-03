package com.forkos.forkos.controller;

import com.forkos.forkos.model.Mesa;
import com.forkos.forkos.repository.MesaRepository;
import com.forkos.forkos.dto.MesaDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para mapear listas

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    // Helper para convertir Entidad a DTO
    private MesaDTO convertToDto(Mesa mesa) {
        MesaDTO mesaDTO = new MesaDTO();
        BeanUtils.copyProperties(mesa, mesaDTO);
        return mesaDTO;
    }

    // Helper para convertir DTO a Entidad (cuando creamos o actualizamos)
    private Mesa convertToEntity(MesaDTO mesaDTO) {
        Mesa mesa = new Mesa();
        BeanUtils.copyProperties(mesaDTO, mesa);
        // Manejo especial si el ID es nulo (para creaciones)
        if (mesaDTO.getId() != null) {
            mesa.setId(mesaDTO.getId());
        }
        return mesa;
    }

    // Endpoint para obtener todas las mesas
    // GET http://localhost:8080/api/mesas
    @GetMapping
    public List<MesaDTO> getAllMesas() {
        return mesaRepository.findAll().stream()
                .map(this::convertToDto) // Convierte cada entidad a DTO
                .collect(Collectors.toList());
    }

    // Endpoint para obtener una mesa por su ID
    // GET http://localhost:8080/api/mesas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MesaDTO> getMesaById(@PathVariable Long id) {
        return mesaRepository.findById(id)
                .map(this::convertToDto) // Convierte la entidad a DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para crear una nueva mesa
    // POST http://localhost:8080/api/mesas
    // Recibe un MesaDTO del frontend
    @PostMapping
    public ResponseEntity<MesaDTO> createMesa(@RequestBody MesaDTO mesaDTO) {
        Mesa mesa = convertToEntity(mesaDTO); // Convierte DTO a Entidad
        // El estado por defecto "LIBRE" ya está en tu entidad Mesa
        Mesa savedMesa = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedMesa)); // Devuelve el DTO
    }

    // Endpoint para actualizar el estado de una mesa
    // PUT http://localhost:8080/api/mesas/{id}/estado
    // Recibe un String con el nuevo estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<MesaDTO> updateMesaEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        Optional<Mesa> mesaOptional = mesaRepository.findById(id);
        if (mesaOptional.isPresent()) {
            Mesa mesa = mesaOptional.get();
            mesa.setEstado(nuevoEstado);
            Mesa updatedMesa = mesaRepository.save(mesa);
            return ResponseEntity.ok(convertToDto(updatedMesa)); // Devuelve el DTO
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para actualizar todos los campos de una mesa
    // PUT http://localhost:8080/api/mesas/{id}
    // Recibe un MesaDTO con los campos actualizados
    @PutMapping("/{id}")
    public ResponseEntity<MesaDTO> updateMesa(@PathVariable Long id, @RequestBody MesaDTO mesaDetailsDTO) {
        Optional<Mesa> mesaOptional = mesaRepository.findById(id);
        if (mesaOptional.isPresent()) {
            Mesa mesa = mesaOptional.get();
            // Actualiza solo los campos que quieres permitir actualizar desde el DTO
            mesa.setNumero(mesaDetailsDTO.getNumero());
            mesa.setCapacidad(mesaDetailsDTO.getCapacidad());
            mesa.setEstado(mesaDetailsDTO.getEstado());
            Mesa updatedMesa = mesaRepository.save(mesa);
            return ResponseEntity.ok(convertToDto(updatedMesa)); // Devuelve el DTO
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar una mesa por su ID
    // DELETE http://localhost:8080/api/mesas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesa(@PathVariable Long id) {
        if (mesaRepository.existsById(id)) {
            mesaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}