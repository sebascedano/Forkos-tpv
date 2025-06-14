// --- MesaService.java ---
package com.forkos.forkos.service;

import com.forkos.forkos.dto.MesaDTO;
import java.util.List;

public interface MesaService {
    List<MesaDTO> obtenerTodasLasMesasConEstadoCalculado();
}