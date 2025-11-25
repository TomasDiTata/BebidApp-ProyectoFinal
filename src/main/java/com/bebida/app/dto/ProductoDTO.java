package com.bebida.app.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Integer stock;
    private String imagenUrl;
    private BigDecimal precioUnitario;
}
