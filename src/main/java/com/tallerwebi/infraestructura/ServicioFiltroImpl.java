package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.RepositorioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Service("servicioFiltro")
@Transactional
public class ServicioFiltroImpl implements ServicioFiltro {

    private final RepositorioMascota repositorioMascota;
    private final RepositorioPublicacion repositorioPublicacion;

    @Autowired
    public ServicioFiltroImpl(RepositorioMascota repositorioMascota,RepositorioPublicacion repositorioPublicacion){
        this.repositorioMascota = repositorioMascota;
        this.repositorioPublicacion= repositorioPublicacion;
    }

    @Override
    public ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo) {

        nombre=validadorCampo(nombre);
        sangre=validadorCampo(sangre);
        tipo=validadorCampo(tipo);

        return new ArrayList<>(repositorioMascota.buscarMascota(nombre, sangre, tipo));
    }

    @Override
    public ArrayList<Publicacion> consultarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion){

        titulo = validadorCampo(titulo);
        tipoDeSangre = validadorCampo(tipoDeSangre);
        zonaDeResidencia = validadorCampo(zonaDeResidencia);
        tipoDePublicacion = validadorCampo(tipoDePublicacion);

        return new ArrayList<>(repositorioPublicacion.buscarPublicaciones(titulo, tipoDeSangre, zonaDeResidencia,tipoDePublicacion));
    }

    private String validadorCampo(String campo) {
        return (campo == null || campo.isEmpty()) ? "" : campo;
    }

}
