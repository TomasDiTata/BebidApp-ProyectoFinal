package com.bebida.app.dto;

//Mostrar informacion completa de una venta "FACTURA"

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;



@Data
public class VentaDTO {
        private Long idVenta;
        private LocalDateTime fecha;
        private Long idCliente;
        private String nombreCliente;
        private BigDecimal total;
        private List<DetalleVentaDTO> detalles;
        private List<PagoDTO> pagos; 
        
    
        
   
  
        
}
