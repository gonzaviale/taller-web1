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
    public void guardarPublicacion(Publicacion publicacion) throws PublicacionNoValida, PublicacionSinTitulo {
        if (publicacion.getTipoDeSangre().isEmpty() && publicacion.getTipoDePublicacion().isEmpty() && publicacion.getTitulo().isEmpty()){
            throw new PublicacionNoValida();
        }
        if(publicacion.getTitulo().isEmpty()){
            throw new PublicacionSinTitulo();
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

    @Override
    public Publicacion busquedaPorId(Long publicacionId) throws PublicacionNoExistente {
        Publicacion publicacionBuscada = repositorioPublicacion.obtenerPorId(publicacionId);
        return publicacionBuscada;
    }

    @Override
    public void desactivarPublicacion(Long publicacionId) {
        repositorioPublicacion.desactivarPublicacion(publicacionId);
    }

    @Override
    public void activarPublicacion(Long publicacionId) {
        repositorioPublicacion.activarPublicacion(publicacionId);

    }

    @Override
    public void editarPublicacion(Long id, Publicacion publicacionActualizada) throws PublicacionNoExistente {
        repositorioPublicacion.editarPublicacion(id,publicacionActualizada);
    }

}
