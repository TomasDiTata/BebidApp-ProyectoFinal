package com.bebida.app.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

//Para crear ventas

@Data
public class VentaRequestDTO {
     @NotNull(message = "El cliente es obligatorio")
    private Long idCliente;
    
    @NotEmpty(message = "La venta debe tener al menos un producto")
    private List<DetalleVentaRequestDTO> detalles;
    
    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago; // "Efectivo", "Tarjeta Debito", etc.

    
    
}
