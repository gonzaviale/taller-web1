package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioMascota {
    ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo);
    void agregarMascota(Mascota mascota);
    Boolean eliminarMascota(Mascota mascota);
    void actualizarMascota(Mascota mascota);
    List<Mascota> buscarMascotaEnRevision();
    void aprobarMascota(Long mascotaId);
    void rechazarMascota(Long mascotaId);
    List<Mascota> obtenerMascotasPorDueno(Usuario dueno);

    Mascota buscarMascotaPorId(Long mascotaId);
}
