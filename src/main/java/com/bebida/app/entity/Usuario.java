package com.bebida.app.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@Data

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    
    
    @Column(name = "dni", unique = true, length = 30, nullable = false)
    private String dni;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "contrasena", length = 100, nullable = false)
    private String contrasena;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;
    
    //@Enumerated (EnumType.STRING)
    //@Column (name = "tipoUsuario", nullable = false)
    //private Tipo_usuario_enum tipoUsuario;
    
    
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

     //🔗 Relación UNO a MUCHOS con usuario_roles
    @OneToMany(mappedBy = "usuario")
    private List<UsuarioRol> usuarioRoles;

    

}

