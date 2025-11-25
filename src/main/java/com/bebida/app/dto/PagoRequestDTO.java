package com.bebida.app.dto;

//Registrar pagos

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;


@Data
public class PagoRequestDTO {
    @NotNull(message = "La venta es obligatoria")
    private Long idVenta;
    
    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;
    
    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
    
    private BigDecimal recargoDescuento = BigDecimal.ZERO;

    
    
}
