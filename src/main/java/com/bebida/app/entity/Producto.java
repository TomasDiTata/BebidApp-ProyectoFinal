package com.bebida.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor

public class Producto {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Long idProducto;
    
    
    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;

    @Column(name = "categoria", length = 20, nullable = false)
    private String categoria;

    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "Stock no puede ser negativo")
    private Integer stock;
    
    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Precio no puede ser negativo")
    private BigDecimal precioUnitario;
    
    
}

