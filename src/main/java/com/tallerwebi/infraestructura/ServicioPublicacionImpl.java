package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.ServicioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("busqueda")
@Transactional
public class ServicioPublicacionImpl implements ServicioPublicacion {
    final private RepositorioPublicacion repositorioPublicacionBusqueda;

    @Autowired
    public ServicioPublicacionImpl(RepositorioPublicacion repositorioPublicacion){
        this.repositorioPublicacionBusqueda = repositorioPublicacion;
    }

    @Override
    public void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        //deberia de consultar en la base de datos por un tipo de sangre valida
        //estan deberian estar precargadas volver mas tarde a realizar esta validacion
        if(publicacion!=null && (publicacion.getTipoDePublicacion() == null && publicacion.getTipoDeSangre()==null) || publicacion.getTipoDeSangre().isEmpty() && publicacion.getTipoDePublicacion().isEmpty()){
            throw new PublicacionNoValida();
        }
        if(publicacion.getTipoDeSangre()==null || publicacion.getTipoDeSangre().isEmpty()){
           throw new PublicacionSinTipoDeSangre();
        }
        if(publicacion.getTipoDePublicacion() == null || publicacion.getTipoDePublicacion().isEmpty() ){
            throw new PublicacionSinTipoDePublicacion();
        }
        repositorioPublicacionBusqueda.guardarPublicacion(publicacion);
    }

    @Override
    public Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente {
        Publicacion publicacionBuscada = repositorioPublicacionBusqueda.obtenerPorId(publicacion.getId());
        if(publicacionBuscada==null){
            throw new PublicacionNoExistente();
        }
        return publicacionBuscada;
    }

}
