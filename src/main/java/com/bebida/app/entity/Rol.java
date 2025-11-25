package com.bebida.app.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false)
    private Long idRol;

    @Enumerated(EnumType.STRING)
    private Tipo_usuario_enum rol;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;

    // 🔗 Relación UNO a MUCHOS con usuario_roles
    @OneToMany(mappedBy = "rol")
    private List<UsuarioRol> usuarioRoles;

}