package com.forkos.forkos.repository;

import com.forkos.forkos.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    // Spring Data JPA ya proporciona findById, findAll, save, deleteById, existsById
    // Puedes añadir métodos personalizados si los necesitas (ej. findByNumero)
}