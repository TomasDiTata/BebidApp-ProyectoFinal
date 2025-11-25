package com.bebida.app.dto;

//Para mostrar los detalles de una venta ya realizada

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaDTO {
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    
    // Constructor útil para convertir desde Entity
    /*public DetalleVentaDTO(Long idProducto, String nombreProducto, Integer cantidad, 
                          BigDecimal precioUnitario, BigDecimal subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }*/
    
    
}
