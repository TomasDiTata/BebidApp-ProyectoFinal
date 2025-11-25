package com.bebida.app.entity;

import lombok.Getter;

@Getter

public enum MetodoPagoEnum {
    EFECTIVO("Efectivo"),
    TARJETA_DEBITO("Tarjeta Debito"),
    TARJETA_CREDITO("Tarjeta Credito"),
    TRANSFERENCIA("Transferencia");
    
    private final String descripcion;
    
    MetodoPagoEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
}
