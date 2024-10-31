package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Usuario;
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
    public void aprobarMascota(Long mascotaId) {
        repositorioMascota.aprobarMascota(mascotaId);
    }

    @Override
    public void rechazarMascota(Long mascotaId) {
        repositorioMascota.rechazarMascota(mascotaId);
    }

    @Override
    public List<Mascota> obtenerMascotasPorDueno(Usuario dueno) {return repositorioMascota.obtenerMascotasPorDueno(dueno);}

    @Override
    public Mascota buscarMascotaPorId(Long mascotaId) {
        return repositorioMascota.buscarMascotaPorId(mascotaId);
    }
}
