package com.bebida.app.repository;

import com.bebida.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    
    @Transactional
    void deleteByDni(String dni);
    
    boolean existsByEmail(String email);
    boolean existsByDni (String dni);
}
