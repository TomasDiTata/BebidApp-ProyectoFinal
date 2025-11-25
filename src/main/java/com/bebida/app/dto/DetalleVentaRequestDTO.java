package com.bebida.app.dto;

//Clase para que el cliente seleccione que y cuantos productos quiere comprar
//Items de una venta


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    @NotNull(message = "El producto es obligatorio")
    private Long idProducto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad; 
    
}
