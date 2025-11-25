package com.bebida.app.repository;

import com.bebida.app.entity.DetalleVentaId;
import com.bebida.app.entity.Detalle_venta;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface  DetalleVentaRepository extends JpaRepository<Detalle_venta, DetalleVentaId>{
    
// Buscar todos los detalles de una venta
    List<Detalle_venta> findById_IdVenta(Long idVenta);

    // Buscar todos los detalles de un producto
    List<Detalle_venta> findById_IdProducto(Long idProducto);

    // Consulta personalizada: productos más vendidos
    @Query("SELECT dv.producto.id, SUM(dv.cantidad) as totalVendido " +
           "FROM Detalle_venta dv " +
           "GROUP BY dv.producto.id " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();

    // Consulta personalizada: detalles de una venta específica
    @Query("SELECT dv FROM Detalle_venta dv WHERE dv.venta.idVenta = :idVenta")
    List<Detalle_venta> findDetallesByVentaId(@Param("idVenta") Long idVenta);

    // Consulta personalizada: total vendido por producto en un rango de fechas
    @Query("SELECT dv.producto.id, SUM(dv.cantidad * dv.precioUnitario) " +
           "FROM Detalle_venta dv " +
           "WHERE dv.venta.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY dv.producto.id")
    List<Object[]> findVentasPorProductoEnPeriodo(@Param("fechaInicio") Date fechaInicio, 
                                                  @Param("fechaFin") Date fechaFin);
}
