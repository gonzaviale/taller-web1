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

    private RepositorioMascota repositorioMascota;

    @Autowired
    public ServicioFiltroImpl(RepositorioMascota repositorioMascota){
        this.repositorioMascota = repositorioMascota;
    }

    @Override
    public ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo) {

        if(nombre!=null && !nombre.isEmpty()){
            nombre = nombre;
        } else {
            nombre = "";
        }
        if(sangre!=null && !sangre.isEmpty()){
            sangre = sangre;
        } else {
            sangre = "";
        }
        if(tipo!=null && !tipo.isEmpty()){
            tipo = tipo;
        } else {
            tipo = "";
        }
        return new ArrayList<>(repositorioMascota.buscarMascota(nombre, sangre, tipo));
    }

}
