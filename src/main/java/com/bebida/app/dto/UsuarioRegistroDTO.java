package com.bebida.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;



@NoArgsConstructor
@Data
public class UsuarioRegistroDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 15, message = "El nombre debe tener entre 2 y 15 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 15, message = "El apellido debe tener entre 2 y 15 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 20, message = "La contraseña debe tener entre 4 y 20 caracteres")
    private String contrasena;
    
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,15}", message = "El DNI debe tener entre 7 y 15 digitos")
    private String dni;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9,15}", message = "El teléfono debe tener entre 9 y 15 dígitos")
    private String telefono;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fechaNacimiento;

    
    
}

