package com.bebida.app.repository;

import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.UsuarioRol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository <UsuarioRol, Long>{
        List<UsuarioRol> findByUsuario(Usuario usuarioRol);

    List<UsuarioRol> findByRol(Rol rol);

    void deleteByUsuario(Usuario usuarioRol);

    void deleteByRol(Rol rol);
}
