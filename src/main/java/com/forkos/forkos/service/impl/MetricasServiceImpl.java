package com.forkos.forkos.service.impl;

import com.forkos.forkos.model.Comanda;
import com.forkos.forkos.model.Turno; // Importa la entidad Turno
import com.forkos.forkos.repository.ComandaRepository;
import com.forkos.forkos.repository.ProductoRepository;
import com.forkos.forkos.service.MetricasService;
import com.forkos.forkos.service.TurnoService; // Importa el TurnoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MetricasServiceImpl implements MetricasService {

    @Autowired
    private ComandaRepository comandaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private TurnoService turnoService; // Inyecta el nuevo servicio de turnos

    private static final int UMBRAL_STOCK_BAJO = 10;

    @Override
    public BigDecimal calcularVentasDelTurnoActual() {
        // Busca el turno activo. Si no hay ninguno, las ventas son 0.
        Optional<Turno> turnoOpt = turnoService.getTurnoActual();
        if (turnoOpt.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Turno turnoActual = turnoOpt.get();

        // Usa las fechas del turno para la consulta
        List<Comanda> comandasDelTurno = comandaRepository.findByEstadoAndFechaHoraCierreBetween(
                "CERRADA",
                turnoActual.getFechaHoraApertura(),
                LocalDateTime.now() // Hasta el momento actual
        );

        return comandasDelTurno.stream()
                .map(Comanda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long contarProductosConStockBajo() {
        // Este método no depende de fechas, así que se queda igual.
        return productoRepository.countByStockLessThanEqual(UMBRAL_STOCK_BAJO);
    }

    @Override
    public Integer getTotalComensalesDelTurnoActual() {
        Optional<Turno> turnoOpt = turnoService.getTurnoActual();
        if (turnoOpt.isEmpty()) {
            return 0;
        }
        Turno turnoActual = turnoOpt.get();

        // Usa las fechas del turno para la consulta
        return comandaRepository.sumCantidadComensalesByEstadoAndFechaHoraCierreBetween(
                "CERRADA",
                turnoActual.getFechaHoraApertura(),
                LocalDateTime.now()
        );
    }

    @Override
    public BigDecimal getTicketPromedioDelTurnoActual() {
        Optional<Turno> turnoOpt = turnoService.getTurnoActual();
        if (turnoOpt.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Turno turnoActual = turnoOpt.get();

        List<Comanda> comandasDelTurno = comandaRepository.findByEstadoAndFechaHoraCierreBetween(
                "CERRADA",
                turnoActual.getFechaHoraApertura(),
                LocalDateTime.now()
        );

        if (comandasDelTurno.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalVentas = comandasDelTurno.stream()
                .map(Comanda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalComensales = comandasDelTurno.stream()
                .mapToInt(Comanda::getCantidadComensales)
                .sum();

        if (totalComensales == 0) {
            return BigDecimal.ZERO;
        }

        return totalVentas.divide(new BigDecimal(totalComensales), 2, RoundingMode.HALF_UP);
    }
}