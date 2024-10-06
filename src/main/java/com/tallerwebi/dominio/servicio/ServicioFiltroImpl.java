package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Service("servicioFiltro")
@Transactional
public class ServicioFiltroImpl implements ServicioFiltro {

    private final RepositorioMascota repositorioMascota;
    private final RepositorioPublicacion repositorioPublicacion;
    private final RepositorioBanco repositorioBanco;

    @Autowired
    public ServicioFiltroImpl(RepositorioMascota repositorioMascota,RepositorioPublicacion repositorioPublicacion, RepositorioBanco repositorioBanco){
        this.repositorioMascota = repositorioMascota;
        this.repositorioPublicacion= repositorioPublicacion;
        this.repositorioBanco=repositorioBanco;
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

    @Override
    public ArrayList<BancoConTiposDeSangre> obtenerCoincidenciasEnBancosDeSangre(String sangreBuscada, String tipoProducto) {

        if (validadorCampo(sangreBuscada).equals(sangreBuscada) && validadorCampo(tipoProducto).isEmpty()) {
            return new ArrayList<>(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos(sangreBuscada));
        }
        if (validadorCampo(sangreBuscada).isEmpty() && validadorCampo(tipoProducto).equals(tipoProducto)) {
            return new ArrayList<>(repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos(tipoProducto));
        }
        if (validadorCampo(sangreBuscada).equals(sangreBuscada) && validadorCampo(tipoProducto).equals(tipoProducto)) {
            return new ArrayList<>(repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(sangreBuscada,tipoProducto));
        }

        return new ArrayList<>(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos(sangreBuscada));
    }


    private String validadorCampo(String campo) {
        return (campo == null || campo.isEmpty()) ? "" : campo;
    }

}
