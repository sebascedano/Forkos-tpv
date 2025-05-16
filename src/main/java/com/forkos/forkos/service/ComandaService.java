package com.forkos.forkos.service;

import com.forkos.forkos.model.Comanda; // Importa la entidad Comanda
import com.forkos.forkos.model.ItemComanda; // Importa la entidad ItemComanda
import com.forkos.forkos.model.Producto; // Importa la entidad Producto (necesaria para agregar items)
import com.forkos.forkos.model.Mesa; // Importa la entidad Mesa (necesaria para crear comanda)
import com.forkos.forkos.model.Usuario; // Importa la entidad Usuario (necesaria para crear comanda)
import com.forkos.forkos.dto.ComandaResponseDTO;
import com.forkos.forkos.dto.ItemComandaResponseDTO;

import java.util.List; // Importa List
import java.util.Optional; // Importa Optional
import java.math.BigDecimal; // Importa BigDecimal para totales y precios

// Interfaz que define el contrato del servicio de Comandas
public interface ComandaService {

    // Operaciones principales con Comandas

    /**
     * Crea una nueva comanda para una mesa y mozo especificados.
     * @param mesaId El ID de la mesa.
     * @param mozoId El ID del mozo (usuario).
     * @return La comanda recién creada.
     */
    ComandaResponseDTO crearComanda(Long mesaId, Long mozoId);

    /**
     * Agrega un ítem (producto con cantidad y notas) a una comanda existente.
     * @param comandaId El ID de la comanda.
     * @param productoId El ID del producto a añadir.
     * @param cantidad La cantidad del producto.
     * @param notas Notas adicionales para el ítem (ej: "sin cebolla").
     * @return El ítem de comanda recién creado.
     */
    ItemComandaResponseDTO agregarItemAComanda(Long comandaId, Long productoId, int cantidad, String notas);

    /**
     * Obtiene una comanda por su ID, incluyendo sus ítems asociados.
     * @param comandaId El ID de la comanda.
     * @return Un Optional que contiene la comanda si se encuentra.
     */
    Optional<ComandaResponseDTO> getComandaById(Long comandaId);

    /**
     * Obtiene todas las comandas (quizás solo las abiertas por defecto).
     * @return Lista de comandas.
     */
    List<ComandaResponseDTO> getAllComandas(); // O tal vez List<Comanda> getComandasAbiertas();

    /**
     * Obtiene todas las comandas abiertas.
     * @return Lista de comandas abiertas.
     */
    List<ComandaResponseDTO> getComandasAbiertas();


    /**
     * Actualiza el estado de una comanda.
     * @param comandaId El ID de la comanda.
     * @param nuevoEstado El nuevo estado (ej: "EN_COCINA", "SERVida").
     * @return La comanda actualizada.
     */
    ComandaResponseDTO updateEstadoComanda(Long comandaId, String nuevoEstado);

    /**
     * Cierra una comanda (por ejemplo, al pagarla), calcula el total final y registra la hora de cierre.
     * @param comandaId El ID de la comanda a cerrar.
     * @return La comanda cerrada.
     */
    ComandaResponseDTO cerrarComanda(Long comandaId);

    /**
     * Elimina un ítem específico de una comanda.
     * @param itemId El ID del ítem a eliminar.
     */
    void eliminarItemComanda(Long itemId);

    /**
     * Elimina una comanda completa (ej: si se cancela antes de enviar a cocina).
     * @param comandaId El ID de la comanda a eliminar.
     */
    void eliminarComanda(Long comandaId);

    // Puedes añadir métodos para, por ejemplo, obtener comandas por mesa, por fecha, etc.
}