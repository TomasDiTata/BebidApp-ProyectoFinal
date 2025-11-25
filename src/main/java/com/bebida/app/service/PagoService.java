package com.bebida.app.service;

import com.bebida.app.dto.PagoDTO;
import com.bebida.app.dto.PagoRequestDTO;
import com.bebida.app.entity.Pago;
import com.bebida.app.entity.Venta;
import com.bebida.app.entity.MetodoPagoEnum;
import com.bebida.app.repository.PagoRepository;
import com.bebida.app.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    private static final BigDecimal RECARGO_TARJETA_CREDITO = new BigDecimal("0.10"); // 10%
    private static final BigDecimal DESCUENTO_EFECTIVO = new BigDecimal("0.05"); // 5%
    
    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerTodos() {
        return pagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        return convertirADTO(pago);
    }
    
    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPorVenta(Long idVenta) {
        return pagoRepository.findByVenta_IdVentaOrderByFechaDesc(idVenta).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PagoDTO crear(PagoRequestDTO pagoRequest) {
        validarPago(pagoRequest);
        
        // Buscar venta
        Venta venta = ventaRepository.findById(pagoRequest.getIdVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + pagoRequest.getIdVenta()));
        
        Pago pago = new Pago();
        pago.setVenta(venta);
        
        // --- CAMBIO CRÍTICO: Usamos valueOf directo ---
        try {
            // El HTML envía "TARJETA_DEBITO", el Enum se llama TARJETA_DEBITO. ¡Match perfecto!
            pago.setMetodoPago(MetodoPagoEnum.valueOf(pagoRequest.getMetodoPago()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Método de pago no válido: " + pagoRequest.getMetodoPago());
        }
        // ----------------------------------------------

        pago.setMonto(pagoRequest.getMonto());
        pago.setFecha(LocalDateTime.now());
        
        // Calcular recargo o descuento
        BigDecimal recargoDescuento = calcularRecargoDescuento(
                pagoRequest.getMonto(), 
                pagoRequest.getMetodoPago() // Enviamos el string tal cual
        );
        pago.setRecargoDescuento(recargoDescuento);
        
        Pago guardado = pagoRepository.save(pago);
        return convertirADTO(guardado);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }
    
    
    private BigDecimal calcularRecargoDescuento(BigDecimal monto, String metodoPago) {
        // Actualizado para usar los nombres en MAYÚSCULAS que envía el HTML
        switch (metodoPago) {
            case "TARJETA_CREDITO":
                return monto.multiply(RECARGO_TARJETA_CREDITO);
            case "EFECTIVO":
                return monto.multiply(DESCUENTO_EFECTIVO).negate();
            default:
                return BigDecimal.ZERO;
        }
    }
    
    // Eliminamos el método convertirStringAEnum porque ya no lo usamos
    
    private void validarPago(PagoRequestDTO dto) {
        if (dto.getIdVenta() == null) {
            throw new RuntimeException("La venta es obligatoria");
        }
        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }
        if (dto.getMetodoPago() == null || dto.getMetodoPago().isEmpty()) {
            throw new RuntimeException("El método de pago es obligatorio");
        }
    }
    
    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdVenta(pago.getVenta().getIdVenta());
        // Usamos .name() para obtener el string técnico (TARJETA_DEBITO)
        // O .getDescripcion() si quieres el bonito ("Tarjeta Debito")
        dto.setMetodoPago(pago.getMetodoPago().getDescripcion()); 
        dto.setMonto(pago.getMonto());
        dto.setRecargoDescuento(pago.getRecargoDescuento());
        dto.setFecha(pago.getFecha());
        return dto;
    }
}