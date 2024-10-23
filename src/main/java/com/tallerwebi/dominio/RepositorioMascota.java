package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioMascota {
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo);
    public void agregarMascota(Mascota mascota);
    public void eliminarMascota(Mascota mascota);
    public void actualizarMascota(Mascota mascota);
    public List<Mascota> buscarMascotaEnRevision();
    public void aprobarMascota(Long mascotaId);
    public void rechazarMascota(Long mascotaId);
    List<Mascota> obtenerMascotasPorDueno(Usuario dueno);

    Mascota buscarMascotaPorId(Long mascotaId);
}
