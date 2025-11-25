package com.bebida.app.repository;

import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Tipo_usuario_enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    Optional<Rol> findByRol(Tipo_usuario_enum rol); // Buscar por 'Administrador', 'Vendedor', 'Cliente'
    
    boolean existsByRol(String rol);
}