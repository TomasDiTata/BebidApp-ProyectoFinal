package com.bebida.app.dto;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private long idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String telefono;
    private Date fechaNacimiento;
    private Integer totalCompras;
    private Double totalGastado;
    private String rol;

    
    //Calculamos edad del cliente
    
    public Integer getEdad(){
        
        LocalDate fechaNac = this.fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        LocalDate hoy = LocalDate.now();
        
        return Period.between(fechaNac, hoy).getYears();
    }

   
    
}
