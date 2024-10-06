package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;

import java.util.List;

public interface ServicioMascota {
    void registrarMascota(Mascota mascota);
    List<Mascota> obtenerMascotasEnRevision();
    void aprobarMascotaDonante(Long mascotaId);
    void rechazarMascotaDonante(Long mascotaId);
}