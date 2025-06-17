package com.forkos.forkos.service;

import java.math.BigDecimal;
public interface MetricasService {
    // Nombres de métodos actualizados para mayor claridad
    BigDecimal calcularVentasDelTurnoActual();
    long contarProductosConStockBajo();
    Integer getTotalComensalesDelTurnoActual();
    BigDecimal getTicketPromedioDelTurnoActual();
}