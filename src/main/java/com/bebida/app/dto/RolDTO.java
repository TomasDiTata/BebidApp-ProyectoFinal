package com.bebida.app.dto;

import lombok.Data;


@Data
public class RolDTO {
    private Long idRol;
    private String rol; //Admin, cliente, vendedor
    private String descripcion; //Admin: Acceso total. Vendedor: elimina, agrega productos, modifica stock. Cliente: Puede realizar compras
}
