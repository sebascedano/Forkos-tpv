package com.forkos.forkos.controller;

import com.forkos.forkos.service.MetricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/metricas")
public class MetricasController {

    @Autowired
    private MetricasService metricasService;

    // Este método manejará las peticiones GET a http://localhost:8080/api/metricas/ventas-dia
    @GetMapping("/ventas-dia")
    public ResponseEntity<BigDecimal> getVentasDelDia() {
        BigDecimal totalVentas = metricasService.calcularVentasDelTurnoActual();
        return ResponseEntity.ok(totalVentas);
    }

    @GetMapping("/stock-bajo/conteo")
    public ResponseEntity<Long> getConteoStockBajo() {
        long conteo = metricasService.contarProductosConStockBajo();
        return ResponseEntity.ok(conteo);
    }


    @GetMapping("/comensales-dia")
    public ResponseEntity<Integer> getTotalComensalesDelDia() {
        return ResponseEntity.ok(metricasService.getTotalComensalesDelTurnoActual());
    }


    @GetMapping("/ticket-promedio")
    public ResponseEntity<BigDecimal> getTicketPromedioDelDia() {
        return ResponseEntity.ok(metricasService.getTicketPromedioDelTurnoActual());
    }

    // En el futuro, podrías añadir aquí otros endpoints de métricas,
    // como el de productos con stock bajo.
}