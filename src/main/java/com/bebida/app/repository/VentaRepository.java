    package com.bebida.app.repository;

import com.bebida.app.entity.Venta;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Obtener las ventas de un cliente ordenadas por fecha de manera descendente
    List<Venta> findByCliente_IdUsuarioOrderByFechaDesc(Long idCliente);
}
