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
        ArrayList<Mascota> mascotas = new ArrayList<>();

        if(!nombre.isEmpty()) {
            ArrayList<Mascota> mascotasPorNombre = repositorioMascota.buscarMascota(nombre);
            if(mascotasPorNombre != null) {
                mascotas.addAll(mascotasPorNombre);
            }
        }

        if(!sangre.isEmpty()) {
            ArrayList<Mascota> mascotasPorSangre = repositorioMascota.buscarPorSangre(sangre);
            if(mascotasPorSangre != null) {
                mascotas.addAll(mascotasPorSangre);
            }
        }

        if(!tipo.isEmpty()) {
            ArrayList<Mascota> mascotasPorTipo = repositorioMascota.buscarPorTipo(tipo);
            if(mascotasPorTipo != null) {
                mascotas.addAll(mascotasPorTipo);
            }
        }

        return mascotas;
    }

}
