package com.bebida.app.dto;
//Clase para actualizar solo el stock. Cosa de que no se pueda actualizar ningun otro dato del producto

import lombok.Data;
import jakarta.validation.constraints.*;


@Data
public class ProductoStockDTO {
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    public Integer getStock() {
        return stock;
    }
    
    
}