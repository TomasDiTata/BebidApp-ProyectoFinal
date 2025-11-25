package com.bebida.app.repository;

import com.bebida.app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStockGreaterThan(int stock);
   // List<Producto> findById(long id);
    //List<Producto> existsById(long id);
    //List<Producto> deleteById(long id);
    List<Producto> findAllByOrderByPrecioUnitarioAsc();
    List<Producto> findAllByOrderByPrecioUnitarioDesc();
    @Query("SELECT p FROM Producto p WHERE p.stock > 0")
    List<Producto> findProductosDisponibles();
}

