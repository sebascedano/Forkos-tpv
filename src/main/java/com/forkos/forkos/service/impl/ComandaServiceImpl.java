package com.forkos.forkos.service.impl;

import com.forkos.forkos.model.Comanda; // Importa entidades y repositorios
import com.forkos.forkos.model.ItemComanda;
import com.forkos.forkos.model.Mesa;
import com.forkos.forkos.model.Producto;
import com.forkos.forkos.model.Usuario;

import com.forkos.forkos.repository.ComandaRepository;
import com.forkos.forkos.repository.ItemComandaRepository;
import com.forkos.forkos.repository.MesaRepository;
import com.forkos.forkos.repository.ProductoRepository;
import com.forkos.forkos.repository.UsuarioRepository;

// Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para transacciones

// Importaciones de Java
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// No usaremos Lombok en esta clase de servicio para el ejemplo

@Service // Indica que es un componente de servicio gestionado por Spring
public class ComandaServiceImpl implements com.forkos.forkos.service.ComandaService { // Implementa la interfaz ComandaService

    // Inyecta los repositorios necesarios
    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository userRepository;
    private final ProductoRepository productoRepository;

    // Constructor para Inyección de Dependencias
    @Autowired
    public ComandaServiceImpl(ComandaRepository comandaRepository,
                              ItemComandaRepository itemComandaRepository,
                              MesaRepository mesaRepository,
                              UsuarioRepository userRepository,
                              ProductoRepository productoRepository) {
        this.comandaRepository = comandaRepository;
        this.itemComandaRepository = itemComandaRepository;
        this.mesaRepository = mesaRepository;
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
    }

    // Implementación de crearComanda(...)
    @Override
    @Transactional // Asegura que la operación sea atómica
    public Comanda crearComanda(Long mesaId, Long mozoId) {
        Optional<Mesa> mesaOpt = mesaRepository.findById(mesaId);
        Optional<Usuario> mozoOpt = userRepository.findById(mozoId);

        if (!mesaOpt.isPresent()) {
            throw new RuntimeException("Mesa con ID " + mesaId + " no encontrada");
        }
        if (!mozoOpt.isPresent()) {
            throw new RuntimeException("Usuario (Mozo) con ID " + mozoId + " no encontrado");
        }

        Comanda nuevaComanda = new Comanda();
        nuevaComanda.setMesa(mesaOpt.get());
        nuevaComanda.setMozo(mozoOpt.get());
        nuevaComanda.setFechaHoraApertura(LocalDateTime.now());
        nuevaComanda.setEstado("ABIERTA"); // Usando String para el estado
        nuevaComanda.setTotal(BigDecimal.ZERO); // Inicializar total

        // La lista de items se inicializará por defecto (usualmente a null o lista vacía por Lombok/JPA)
        // Si usas Lombok @AllArgsConstructor, asegúrate que pueda manejar la inicialización de listas o usa @Builder

        return comandaRepository.save(nuevaComanda);
    }

    // Implementación de agregarItemAComanda(...)
    @Override
    @Transactional
    public ItemComanda agregarItemAComanda(Long comandaId, Long productoId, int cantidad, String notas) {
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }
        if (!productoOpt.isPresent()) {
            throw new RuntimeException("Producto con ID " + productoId + " no encontrado");
        }
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero");
        }

        Comanda comanda = comandaOpt.get();
        Producto producto = productoOpt.get();

        // Validar que la comanda esté en un estado que permita añadir items (ej: ABIERTA)
        if (!"ABIERTA".equals(comanda.getEstado())) {
            throw new RuntimeException("No se pueden añadir items a una comanda que no está ABIERTA.");
        }

        ItemComanda nuevoItem = new ItemComanda();
        nuevoItem.setComanda(comanda); // Establece la relación bidireccional
        nuevoItem.setProducto(producto);
        nuevoItem.setCantidad(cantidad);
        nuevoItem.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio())); // Precio al momento de añadir
        nuevoItem.setNotas(notas);
        nuevoItem.setEstado("PEDIDO"); // Estado inicial del ítem (puedes usar otros estados para items si es relevante)

        // Opcional: Asegurar la relación bidireccional en la lista de la comanda
        // Si la lista 'items' en Comanda es null, inicialízala.
        if (comanda.getItems() == null) {
            comanda.setItems(new java.util.ArrayList<>());
        }
        comanda.getItems().add(nuevoItem); // Añade el item a la lista de la comanda

        // Guardar el nuevo ítem. Spring Data JPA debería gestionar la relación.
        ItemComanda savedItem = itemComandaRepository.save(nuevoItem);

        // Recalcular y guardar el total de la comanda después de añadir el item (opcional, podrías hacerlo al cerrar)
        // Pero es mejor actualizar el total aquí para tenerlo siempre al día
        BigDecimal totalActualizado = calcularTotalComanda(comanda); // Llama a un método auxiliar
        comanda.setTotal(totalActualizado);
        comandaRepository.save(comanda); // Guarda la comanda con el total actualizado

        // Opcional (para fases futuras): Descontar stock de ingredientes si aplica

        return savedItem;
    }

    // Método auxiliar para calcular el total de una comanda
    private BigDecimal calcularTotalComanda(Comanda comanda) {
        BigDecimal total = BigDecimal.ZERO;
        if (comanda.getItems() != null) {
            for (ItemComanda item : comanda.getItems()) {
                // Asegúrate de manejar items cancelados si es necesario (sumar solo los no cancelados)
                if (!"CANCELADO".equals(item.getEstado())) { // Ejemplo: No sumar items cancelados
                    // Asegúrate de que precioUnitario y cantidad no sean null
                    BigDecimal precioItemTotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
                    total = total.add(precioItemTotal);
                }
            }
        }
        return total;
    }


    // Implementación de getComandaById(...)
    @Override
    @Transactional(readOnly = true)
    public Optional<Comanda> getComandaById(Long comandaId) {
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        // Si la comanda existe y los items son LAZY, acceder a ellos aquí para forzar la carga dentro de la transacción
        comandaOpt.ifPresent(comanda -> {
            if (comanda.getItems() != null) {
                comanda.getItems().size(); // Fuerza la inicialización de la colección LAZY
            }
        });
        return comandaOpt;
    }

    // Implementación de getAllComandas()
    @Override
    @Transactional(readOnly = true)
    public List<Comanda> getAllComandas() {
        return comandaRepository.findAll();
    }

    // Implementación de getComandasAbiertas()
    @Override
    @Transactional(readOnly = true)
    public List<Comanda> getComandasAbiertas() {
        // Usa el método del repositorio para encontrar por estado String
        return comandaRepository.findByEstado("ABIERTA"); // Usando String "ABIERTA"
    }


    // Implementación de updateEstadoComanda(...)
    @Override
    @Transactional
    public Comanda updateEstadoComanda(Long comandaId, String nuevoEstado) { // Recibe el estado como String
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }

        Comanda comanda = comandaOpt.get();

        // Aquí iría la lógica de validación de transiciones de estado si es necesario
        // ej: if ("CERRADA".equals(comanda.getEstado())) { throw new RuntimeException("No se puede cambiar el estado de una comanda cerrada"); }
        // Y verificar si el nuevoEstadoString es un estado válido (ej: "ABIERTA", "PENDIENTE_PAGO", "CERRADA")
        // Una forma simple de validar si te saltaste el Enum:
        List<String> estadosValidos = java.util.Arrays.asList("ABIERTA", "PENDIENTE_PAGO", "CERRADA"); // Añade CANCELADA si aplica
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new RuntimeException("Estado '" + nuevoEstado + "' no es un estado de comanda válido.");
        }


        comanda.setEstado(nuevoEstado); // Establece el estado usando el String
        return comandaRepository.save(comanda); // Guarda la comanda actualizada
    }

    // Implementación de cerrarComanda(...) - Transición final a CERRADA
    @Override
    @Transactional
    public Comanda cerrarComanda(Long comandaId) {
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }

        Comanda comanda = comandaOpt.get();

        // Opcional: Validar que la comanda esté en un estado previo que permita cerrarse (ej: PENDIENTE_PAGO o SERVIDA si usas ese estado)
        // if (!"PENDIENTE_PAGO".equals(comanda.getEstado())) {
        //      throw new RuntimeException("La comanda " + comandaId + " no está en estado PENDIENTE_PAGO para cerrarse.");
        // }

        // Calcular el total (ya tenemos el método auxiliar)
        BigDecimal totalCalculado = calcularTotalComanda(comanda);

        comanda.setFechaHoraCierre(LocalDateTime.now()); // Registrar hora de cierre
        comanda.setEstado("CERRADA"); // Establecer estado final CERRADA
        comanda.setTotal(totalCalculado); // Establecer el total calculado

        return comandaRepository.save(comanda); // Guardar los cambios
    }

    // Implementación de eliminarItemComanda(...)
    @Override
    @Transactional
    public void eliminarItemComanda(Long itemId) {
        Optional<ItemComanda> itemOpt = itemComandaRepository.findById(itemId);
        if (!itemOpt.isPresent()) {
            throw new RuntimeException("Item de comanda con ID " + itemId + " no encontrado");
        }

        ItemComanda item = itemOpt.get();
        Comanda comanda = item.getComanda(); // Obtiene la comanda a la que pertenece

        itemComandaRepository.deleteById(itemId); // Elimina el item

        // Opcional: Recalcular el total de la comanda después de eliminar un item
        // Esto es importante si quieres que el total se actualice inmediatamente
        BigDecimal totalActualizado = calcularTotalComanda(comanda);
        comanda.setTotal(totalActualizado);
        comandaRepository.save(comanda); // Guarda la comanda con el total actualizado
    }

    // Implementación de eliminarComanda(...)
    @Override
    @Transactional
    public void eliminarComanda(Long comandaId) {
        // Opcional: Verificar si la comanda existe
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }

        // La eliminación de la comanda eliminará automáticamente sus ItemComanda asociados
        // gracias a la configuración cascade y orphanRemoval en la entidad Comanda.
        comandaRepository.deleteById(comandaId);
    }

    // Puedes añadir implementaciones para otros métodos de ComandaService aquí
}