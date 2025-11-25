package com.bebida.app.repository;

import com.bebida.app.entity.MetodoPagoEnum;
import com.bebida.app.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    //Buscar pagos por venta, ordenados por fecha
    List<Pago> findByVenta_IdVentaOrderByFechaDesc(Long idVenta);
    
    //Buscar todos los pagos ordenados por fecha (para historial)
    List<Pago> findAllByOrderByFechaDesc();
    
    //Buscar pagos por método de pago y ordenar por fecha
    List<Pago> findByMetodoPagoOrderByFechaDesc(MetodoPagoEnum metodoPago);
    
    List<Pago> findByFechaBetween(Date inicio, Date fin);
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fecha BETWEEN :inicio AND :fin")
    Double calcularTotalPagadoEnPeriodo(@Param("inicio") Date inicio, @Param("fin") Date fin);
}
