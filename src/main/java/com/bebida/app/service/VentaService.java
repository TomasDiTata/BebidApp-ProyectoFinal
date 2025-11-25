package com.bebida.app.service;

import com.bebida.app.dto.VentaDTO;
import com.bebida.app.dto.VentaRequestDTO;
import com.bebida.app.dto.DetalleVentaDTO;
import com.bebida.app.dto.DetalleVentaRequestDTO;
import com.bebida.app.dto.PagoDTO; // Asegúrate de importar esto
import com.bebida.app.entity.DetalleVentaId;
import com.bebida.app.entity.Detalle_venta;
import com.bebida.app.entity.Pago;
import com.bebida.app.entity.Producto;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.Venta;
import com.bebida.app.repository.DetalleVentaRepository;
import com.bebida.app.repository.PagoRepository;
import com.bebida.app.repository.ProductoRepository;
import com.bebida.app.repository.UsuarioRepository;
import com.bebida.app.repository.VentaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired 
    private ProductoRepository productoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired 
    private ProductoService productoService;
    
    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerTodas(){
        return ventaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VentaDTO obtenerPorId(Long id){
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
        return convertirADTO(venta);
    }
    
    // Este método es para la API, el Controller usa el repositorio directo para Historial (está bien)
    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerPorCliente(Long idCliente){
        return ventaRepository.findByCliente_IdUsuarioOrderByFechaDesc(idCliente).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public VentaDTO crear(VentaRequestDTO ventaRequest){
        validarVenta(ventaRequest);
        
        Usuario cliente = usuarioRepository.findById(ventaRequest.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + ventaRequest.getIdCliente()));
    
        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setCliente(cliente);
        venta.setTotal(BigDecimal.ZERO);
        
        Venta ventaGuardada = ventaRepository.save(venta);
        
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaRequestDTO detalleRequest : ventaRequest.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleRequest.getIdProducto()));
            
            if (producto.getStock() < detalleRequest.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            
            BigDecimal subtotal = producto.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleRequest.getCantidad()));
            
            DetalleVentaId detalleId = new DetalleVentaId();
            detalleId.setIdVenta(ventaGuardada.getIdVenta());
            detalleId.setIdProducto(producto.getIdProducto());
            
            Detalle_venta detalle = new Detalle_venta();
            detalle.setId(detalleId);
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
            
            detalleVentaRepository.save(detalle);
            productoService.actualizarStock(producto.getIdProducto(), -detalleRequest.getCantidad());
            
            total = total.add(subtotal);
        }
        
        ventaGuardada.setTotal(total);
        ventaRepository.save(ventaGuardada);
        
        return convertirADTO(ventaGuardada);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con id: " + id);
        }
        ventaRepository.deleteById(id);
    }
    
    private void validarVenta(VentaRequestDTO dto) {
        if (dto.getIdCliente() == null) {
            throw new RuntimeException("El cliente es obligatorio");
        }
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }
    }
    
    // --- MÉTODO CRÍTICO: CONVERTIR A DTO ---
    private VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setFecha(venta.getFecha());
        
        if (venta.getCliente() != null) {
            dto.setIdCliente(venta.getCliente().getIdUsuario());
            dto.setNombreCliente(venta.getCliente().getNombre() + " " + venta.getCliente().getApellido());
        }
        
        dto.setTotal(venta.getTotal());

        // 1. Mapear Detalles (Productos)
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            dto.setDetalles(venta.getDetalles().stream()
                    .map(this::convertirDetalleADTO)
                    .collect(Collectors.toList()));
        } else {
            // Si Hibernate no trajo la lista, probamos buscarla (Fallback)
            List<Detalle_venta> detalles = detalleVentaRepository.findById_IdVenta(venta.getIdVenta());
            if (detalles != null) {
                dto.setDetalles(detalles.stream()
                        .map(this::convertirDetalleADTO)
                        .collect(Collectors.toList()));
            } else {
                dto.setDetalles(new ArrayList<>());
            }
        }

        // 2. Mapear Pagos (¡IMPORTANTE!)
        if (venta.getPagos() != null && !venta.getPagos().isEmpty()) {
            dto.setPagos(venta.getPagos().stream()
                    .map(this::convertirPagoADTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setPagos(new ArrayList<>());
        }

        return dto;
    }
    
    private DetalleVentaDTO convertirDetalleADTO(Detalle_venta detalle) {
        return new DetalleVentaDTO(
            detalle.getProducto().getIdProducto(),
            detalle.getProducto().getNombre(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getSubtotal()
        );
    }
    
    private PagoDTO convertirPagoADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        
        // Usamos el getter correcto (Lombok genera getIdPago)
        dto.setIdPago(pago.getIdPago()); 
        dto.setIdVenta(pago.getVenta().getIdVenta());
        dto.setMonto(pago.getMonto());
        dto.setRecargoDescuento(pago.getRecargoDescuento());
        dto.setFecha(pago.getFecha());
        
        // Validación de seguridad para el Enum
        if (pago.getMetodoPago() != null) {
            dto.setMetodoPago(pago.getMetodoPago().name());
        } else {
            dto.setMetodoPago("DESCONOCIDO");
        }
        return dto;
    }
}