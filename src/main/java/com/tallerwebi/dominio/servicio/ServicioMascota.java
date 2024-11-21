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

    Boolean eliminarMascota(Mascota mascotaBuscada);

    List<String> obtenerSangreSegunTipoDeMascota(String tipo);

    void editarMascota(Mascota mascotaBuscada);

    boolean isPesoCorrectoCanino(Mascota mascotaBuscada);

    boolean isEdadApropiadaDonante(Mascota mascota);

    boolean isPesoCorrectoFelino(Mascota mascota);
}
