package com.bebida.app.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CrearProductoDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 50, message = "La descripción no puede tener más de 50 caracteres")
    private String descripcion;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 20, message = "La categoría no puede tener más de 20 caracteres")
    private String categoria;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 255, message = "La URL de la imagen no puede ser tan larga")
    private String imagenUrl;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioUnitario;

    
}

