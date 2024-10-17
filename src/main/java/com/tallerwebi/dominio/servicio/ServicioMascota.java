package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioMascota {
    void registrarMascota(Mascota mascota);
    List<Mascota> obtenerMascotasEnRevision();
    void aprobarMascota(Long mascotaId);
    void rechazarMascota(Long mascotaId);

    List<Mascota> obtenerMascotasPorDueno(Usuario dueno);

    Mascota buscarMascotaPorId(Long mascotaId);
}
