package com.bebida.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor

public class Detalle_venta{
    
    @EmbeddedId
    
    private DetalleVentaId id;
    
    
    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idVenta")
    @JoinColumn(name = "id_venta")
    private Venta venta;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @Column(name = "cantidad",nullable = false)
    private Integer cantidad;
    
    
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal;

}

