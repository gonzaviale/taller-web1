package com.tallerwebi.presentacion;

import lombok.Data;

@Data
public class BancoConTiposDeSangre {

    private Long bancoId;
    private Long sangreId;
    private String tipoProducto;
    private String tipoSangre;
    private int cantidad;
    private String nombreBanco;
    private String direccion;
    private String ciudad;
    private String pais;
    private String telefono;
    private String email;

}
