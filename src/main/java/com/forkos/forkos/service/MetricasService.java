package com.forkos.forkos.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface MetricasService {
    // Nombres de métodos actualizados para mayor claridad
    BigDecimal calcularVentasDelTurnoActual();
    long contarProductosConStockBajo();
    Integer getTotalComensalesDelTurnoActual();
    BigDecimal getTicketPromedioDelTurnoActual();
}