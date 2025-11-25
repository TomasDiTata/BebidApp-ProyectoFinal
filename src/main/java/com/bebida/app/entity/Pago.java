package com.bebida.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
    @Table(name = "pagos")
    @NoArgsConstructor
    @Data
public class Pago{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago", nullable = false)
    private Long idPago;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", referencedColumnName = "id_venta", nullable = false)
    private Venta venta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPagoEnum metodoPago;

    @Column(name = "monto", precision = 30, scale = 2, nullable = false)
    private BigDecimal monto;
    
    @Column(name = "recargo_descuento", precision = 10, scale = 2, nullable = false)
    private BigDecimal recargoDescuento;
    
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    
    
    
}

