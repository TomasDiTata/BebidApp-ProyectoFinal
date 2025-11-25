package com.bebida.app.dto;

import lombok.Data;



@Data
public class UsuarioRolDTO {
    private Long idUsuarioRol;
    private UsuarioDTO usuario; //Objeto completo del usuario
    private RolDTO rol; //Objeto completo del rol que desempeña dicho usuario
}
