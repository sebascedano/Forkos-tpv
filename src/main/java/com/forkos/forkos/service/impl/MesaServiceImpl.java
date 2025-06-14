// --- MesaServiceImpl.java ---
package com.forkos.forkos.service.impl;

import com.forkos.forkos.dto.MesaDTO;
import com.forkos.forkos.model.Mesa;
import com.forkos.forkos.repository.ComandaRepository;
import com.forkos.forkos.repository.MesaRepository;
import com.forkos.forkos.service.MesaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaServiceImpl implements MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ComandaRepository comandaRepository;

    @Override
    public List<MesaDTO> obtenerTodasLasMesasConEstadoCalculado() {
        // 1. Obtener todas las entidades Mesa de la BD
        List<Mesa> mesas = mesaRepository.findAll();

        // 2. Convertir cada Mesa a DTO, calculando el estado real
        return mesas.stream()
                .map(this::convertirADtoConEstado)
                .collect(Collectors.toList());
    }

    private MesaDTO convertirADtoConEstado(Mesa mesa) {
        // 3. Verificar si existe una comanda "ABIERTA" para esta mesa
        boolean estaOcupada = comandaRepository.existsByMesaIdAndEstado(mesa.getId(), "ABIERTA");

        // 4. Crear el DTO y establecer el estado correcto
        MesaDTO dto = new MesaDTO();
        BeanUtils.copyProperties(mesa, dto); // Copia id, numero, capacidad
        dto.setEstado(estaOcupada ? "OCUPADA" : "LIBRE"); // Asigna el estado calculado

        return dto;
    }
}