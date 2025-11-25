package com.bebida.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

//Mostrar informacion de pago

@Data
public class PagoDTO {
    
    private Long idPago;
    private Long idVenta;
    private String metodoPago;
    private BigDecimal monto;
    private BigDecimal recargoDescuento;
    private LocalDateTime fecha;
    private BigDecimal montoTotal; // monto + recargo_descuento
    
    // Método calculado
    public BigDecimal getMontoTotal() {
        if (monto != null && recargoDescuento != null) {
            return monto.add(recargoDescuento);
        }
        return monto;
    }

    
}
