package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("publicacion")
@Transactional
public class ServicioPublicacionImpl implements ServicioPublicacion {
    final private RepositorioPublicacion repositorioPublicacion;

    @Autowired
    public ServicioPublicacionImpl(RepositorioPublicacion repositorioPublicacion){
        this.repositorioPublicacion = repositorioPublicacion;
    }

    @Override
    public void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //deberia de consultar en la base de datos por un tipo de sangre valida
        //estan deberian estar precargadas volver mas tarde a realizar esta validacion
        if (publicacion.getTipoDeSangre().isEmpty() && publicacion.getTipoDePublicacion().isEmpty() && publicacion.getTitulo().isEmpty()){
            throw new PublicacionNoValida();
        }
        if(publicacion.getTitulo().isEmpty()){
            throw new PublicacionSinTitulo();
        }
        if(publicacion.getTipoDeSangre().isEmpty()){
           throw new PublicacionSinTipoDeSangre();
        }
        if(publicacion.getTipoDePublicacion().isEmpty() ){
            throw new PublicacionSinTipoDePublicacion();
        }
        publicacion.activar();
        repositorioPublicacion.guardarPublicacion(publicacion);
    }

    @Override
    public Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente {
        Publicacion publicacionBuscada = repositorioPublicacion.obtenerPorId(publicacion.getId());
        if(publicacionBuscada==null){
            throw new PublicacionNoExistente();
        }
        return publicacionBuscada;
    }

    @Override
    public List<Publicacion> obtenerTodasLasPublicaciones() {
        return repositorioPublicacion.obtenerTodasLasPublicaciones();
    }

}
