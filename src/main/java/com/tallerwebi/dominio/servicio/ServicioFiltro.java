package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;

import java.util.ArrayList;

public interface ServicioFiltro {

    ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo);
    ArrayList<Publicacion> consultarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion);

    ArrayList<BancoConTiposDeSangre> obtenerCoincidenciasEnBancosDeSangre(String sangreBuscada, String tipoProducto);
}