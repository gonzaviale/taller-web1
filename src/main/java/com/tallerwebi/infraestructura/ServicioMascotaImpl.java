package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioMascota")
@Transactional
public class ServicioMascotaImpl implements ServicioMascota {

    final private RepositorioMascota repositorioMascota;

    @Autowired
    public ServicioMascotaImpl(RepositorioMascota repositorioMascota){this.repositorioMascota = repositorioMascota;}

    @Override
    public void registrarMascota(Mascota mascota) {
        repositorioMascota.agregarMascota(mascota);
    }

    @Override
    public List<Mascota> obtenerMascotasEnRevision() {
        return repositorioMascota.buscarMascotaEnRevision();
    }

    @Override
    public void aprobarMascotaDonante(Long mascotaId) {
        repositorioMascota.aprobarMascotaDonante(mascotaId);
    }

    @Override
    public void rechazarMascotaDonante(Long mascotaId) {
        repositorioMascota.rechazarMascotaDonante(mascotaId);
    }
}
