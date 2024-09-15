package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import java.util.ArrayList;
import java.util.List;

public interface ServicioFiltro {

    ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo);

}