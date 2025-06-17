package com.forkos.forkos.service.impl;

import com.forkos.forkos.dto.ComandaResponseDTO;
import com.forkos.forkos.dto.ItemComandaResponseDTO;
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
import java.util.stream.Collectors;

@Service
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

    // --- Métodos de Mapeo (Entidad a DTO) ---
    // Estos métodos son privados porque son ayudantes internos del servicio
    private ItemComandaResponseDTO mapItemComandaToDTO(ItemComanda item) {
        ItemComandaResponseDTO dto = new ItemComandaResponseDTO();
        dto.setId(item.getId());
        dto.setCantidad(item.getCantidad());
        dto.setNotas(item.getNotas());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setEstado(item.getEstado());

        if (item.getProducto() != null) { // Verifica si la relación Producto está cargada
            dto.setProductoId(item.getProducto().getId());
            dto.setProductoNombre(item.getProducto().getNombre()); // Asumiendo que Producto tiene getNombre()
            // Puedes añadir más campos del producto si los necesitas en el DTO y la entidad Producto tiene sus getters
            // dto.setProductoDescripcion(item.getProducto().getDescripcion()); // Ejemplo
        }
        return dto;
    }

    private ComandaResponseDTO mapComandaToDTO(Comanda comanda) {
        ComandaResponseDTO dto = new ComandaResponseDTO();
        dto.setId(comanda.getId());
        dto.setFechaHoraApertura(comanda.getFechaHoraApertura());
        dto.setFechaHoraCierre(comanda.getFechaHoraCierre());
        dto.setEstado(comanda.getEstado());
        dto.setTotal(comanda.getTotal());

        // Mapear información de Mesa (accede a comanda.getMesa() AQUI, dentro de un método @Transactional si es LAZY)
        if (comanda.getMesa() != null) { // Verifica si la relación Mesa está cargada
            dto.setMesaId(comanda.getMesa().getId());
            dto.setMesaNumero(comanda.getMesa().getNumero()); // Asumiendo que Mesa tiene getNumero()
            // Puedes añadir más campos de Mesa si los necesitas
        }

        // Mapear información de Usuario (Mozo) (accede a comanda.getMozo() AQUI, dentro de un método @Transactional si es LAZY)
        if (comanda.getMozo() != null) { // Verifica si la relación Usuario está cargada
            dto.setMozoId(comanda.getMozo().getId());
            dto.setMozoNombre(comanda.getMozo().getNombre()); // Asumiendo que Usuario tiene getNombre()
            // Puedes añadir más campos de Usuario si los necesitas
        }

        // Mapear la lista de ítems (accede a comanda.getItems() AQUI, dentro de un método @Transactional si es LAZY)
        if (comanda.getItems() != null) { // Verifica si la lista de ítems está cargada
            // Usa stream() y map() para convertir cada ItemComanda en la lista a un ItemComandaResponseDTO
            dto.setItems(comanda.getItems().stream()
                    .map(this::mapItemComandaToDTO) // Llama al método de mapeo de ItemComanda para cada item
                    .collect(Collectors.toList())); // Recolecta los resultados en una nueva lista de DTOs
        } else {
            // Si la lista de items en la entidad es null (lo cual no debería pasar si la inicializas), asegúrate de que la lista en el DTO no sea null.
            dto.setItems(new java.util.ArrayList<>());
        }

        return dto; // Retorna el DTO de Comanda
    }
    // --- Fin de los Métodos de Mapeo ---

    // Implementación de crearComanda(...)
    @Override
    @Transactional
    public ComandaResponseDTO crearComanda(Long mesaId, Long mozoId, Integer cantidadComensales) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new RuntimeException("Mesa con ID " + mesaId + " no encontrada"));

        // VALIDACIÓN DEL ESTADO DE LA MESA USANDO STRINGS
        boolean yaTieneComandaAbierta = comandaRepository.existsByMesaIdAndEstado(mesaId, "ABIERTA");
        if (yaTieneComandaAbierta) {
            throw new IllegalStateException("La mesa " + mesa.getNumero() + " ya tiene una comanda abierta.");
        }

        Usuario mozo = userRepository.findById(mozoId)
                .orElseThrow(() -> new RuntimeException("Usuario (Mozo) con ID " + mozoId + " no encontrado"));

        // VALIDACIÓN DE CANTIDAD DE COMENSALES
        if (cantidadComensales == null || cantidadComensales <= 0 || cantidadComensales > mesa.getCapacidad()) {
            throw new RuntimeException("Número de comensales inválido para la mesa " + mesa.getNumero() + ". Debe ser entre 1 y " + mesa.getCapacidad() + ".");
        }

        Comanda nuevaComanda = new Comanda();
        nuevaComanda.setMesa(mesa);
        nuevaComanda.setMozo(mozo);
        nuevaComanda.setFechaHoraApertura(LocalDateTime.now());
        nuevaComanda.setTotal(BigDecimal.ZERO);
        nuevaComanda.setItems(new java.util.ArrayList<>());
        nuevaComanda.setEstado("ABIERTA"); // Asignar el String directamente
        nuevaComanda.setCantidadComensales(cantidadComensales); // Guardar la cantidad de comensales

        Comanda savedComanda = comandaRepository.save(nuevaComanda);

        // ACTUALIZAR EL ESTADO DE LA MESA A OCUPADA USANDO STRING
        mesa.setEstado("OCUPADA"); // Asignar el String directamente
        mesaRepository.save(mesa);

        return mapComandaToDTO(savedComanda);
    }

    // Implementación de agregarItemAComanda(...)
    @Override
    @Transactional
    public ItemComandaResponseDTO agregarItemAComanda(Long comandaId, Long productoId, int cantidad, String notas) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda con ID " + comandaId + " no encontrada"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto con ID " + productoId + " no encontrado"));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (!"ABIERTA".equals(comanda.getEstado())) {
            throw new IllegalStateException("No se pueden añadir items a una comanda que no está ABIERTA.");
        }

        // --- INICIO DE LA NUEVA LÓGICA DE STOCK ---

        // 1. Validación: Comprobamos si hay stock suficiente.
        if (producto.getStock() < cantidad) {
            // Si no hay, lanzamos un error claro que el frontend podrá mostrar.
            throw new IllegalStateException("No hay stock suficiente para '" + producto.getNombre() + "'. Stock actual: " + producto.getStock());
        }

        // 2. Reducción: Si hay stock, lo disminuimos.
        producto.setStock(producto.getStock() - cantidad);
        // No es necesario llamar a productoRepository.save(producto) gracias a @Transactional.
        // Hibernate detectará el cambio y actualizará el producto en la base de datos automáticamente.

        // --- FIN DE LA NUEVA LÓGICA DE STOCK ---

        ItemComanda nuevoItem = new ItemComanda();
        nuevoItem.setComanda(comanda);
        nuevoItem.setProducto(producto);
        nuevoItem.setCantidad(cantidad);
        nuevoItem.setPrecioUnitario(producto.getPrecio());
        nuevoItem.setNotas(notas);
        nuevoItem.setEstado("NUEVO"); // Usamos "NUEVO" como estado inicial claro

        if (comanda.getItems() == null) {
            comanda.setItems(new java.util.ArrayList<>());
        }
        comanda.getItems().add(nuevoItem);

        // Guardar el nuevo ítem nos devuelve la instancia ya persistida con su ID
        ItemComanda savedItem = itemComandaRepository.save(nuevoItem);

        // Recalcular y guardar el total de la comanda
        comanda.setTotal(calcularTotalComanda(comanda));
        comandaRepository.save(comanda);

        // Usamos directamente el 'savedItem' que ya tiene el ID, es más simple.
        return mapItemComandaToDTO(savedItem);
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
    public Optional<ComandaResponseDTO> getComandaById(Long comandaId) {
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        // Si la comanda existe y los items son LAZY, acceder a ellos aquí para forzar la carga dentro de la transacción
        comandaOpt.ifPresent(comanda -> {
            if (comanda.getItems() != null) {
                comanda.getItems().size(); // Fuerza la inicialización de la colección LAZY
            }
        });
        return comandaOpt.map(this::mapComandaToDTO);
    }

    // Implementación de getAllComandas()
    @Override
    @Transactional(readOnly = true)
    public List<ComandaResponseDTO> getAllComandas() {
        List<Comanda> comandas=comandaRepository.findAll();
        return comandas.stream()
                .map(this::mapComandaToDTO) // Mapea cada Comanda a su DTO
                .collect(Collectors.toList()); // Recolecta en una nueva lista de DTOs
    }

    // Implementación de getComandasAbiertas()
    @Override
    @Transactional(readOnly = true)
    public List<ComandaResponseDTO> getComandasAbiertas() {
        List<Comanda> comandas=comandaRepository.findByEstado("ABIERTA");// Usando String "ABIERTA"
        return comandas.stream().map(this::mapComandaToDTO).collect(Collectors.toList());
    }


    // Implementación de updateEstadoComanda(...)
    @Override
    @Transactional // Mantén Transactional
    public ComandaResponseDTO updateEstadoComanda(Long comandaId, String nuevoEstado) { // Cambia tipo de retorno a DTO
        // ... (Tu lógica existente para encontrar la comanda y validar el estado) ...
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }
        Comanda comanda = comandaOpt.get();
        // ... (Validación del estado, ej: si es un estado válido en tu lógica) ...

        comanda.setEstado(nuevoEstado);
        // Guarda la comanda actualizada
        Comanda updatedComanda = comandaRepository.save(comanda);

        // --- === Mapear la entidad guardada a DTO === ---
        // Llama al método de mapeo AQUI, dentro del método @Transactional.
        return mapComandaToDTO(updatedComanda); // Llama al método de mapeo
        // --- Fin del mapeo ---
    }

    // Implementación de cerrarComanda(...) - Transición final a CERRADA
    @Override
    @Transactional // Mantén Transactional
    public ComandaResponseDTO cerrarComanda(Long comandaId) {
        Optional<Comanda> comandaOpt = comandaRepository.findById(comandaId);
        if (!comandaOpt.isPresent()) {
            throw new RuntimeException("Comanda con ID " + comandaId + " no encontrada");
        }
        Comanda comanda = comandaOpt.get();

        comanda.setEstado("CERRADA"); // O el nombre del estado CERRADA
        comanda.setFechaHoraCierre(LocalDateTime.now());

        // --- ACTUALIZA EL ESTADO DE LA MESA ASOCIADA ---
        Mesa mesaAsociada = comanda.getMesa();
        if (mesaAsociada != null) {
            mesaAsociada.setEstado("LIBRE");
            mesaRepository.save(mesaAsociada);
        }

        // Guarda la comanda cerrada
        Comanda closedComanda = comandaRepository.save(comanda);
        // --- === Mapear la entidad guardada a DTO === ---
        return mapComandaToDTO(closedComanda);
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

        boolean removed = comanda.getItems().remove(item); // Elimina el item de la lista

        // Opcional: Recalcular el total de la comanda después de eliminar un item
        // Esto es importante si quieres que el total se actualice inmediatamente
        BigDecimal totalActualizado = calcularTotalComanda(comanda);
        comanda.setTotal(totalActualizado);
        comandaRepository.save(comanda); // Guarda la comanda con el total actualizado
    }

    // Implementacion getComandaAbiertaPorMesaId
    @Override
    @Transactional(readOnly = true) // Es una operación de lectura
    public Optional<ComandaResponseDTO> getComandaAbiertaPorMesaId(Long mesaId) {
        // Usa el método que definimos en ComandaRepository
        Optional<Comanda> comandaOpt = comandaRepository.findByMesaIdAndEstado(mesaId, "ABIERTA");

        // Si la comanda existe, fuerza la carga de los ítems (si son LAZY)
        comandaOpt.ifPresent(comanda -> {
            if (comanda.getItems() != null) {
                comanda.getItems().size(); // Esto fuerza la inicialización de la colección LAZY
            }
        });

        // Mapea la entidad Comanda a ComandaResponseDTO si se encuentra
        return comandaOpt.map(this::mapComandaToDTO);
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

}