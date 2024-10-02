package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioMascota {
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo);
    public void agregarMascota(Mascota mascota);
    public void eliminarMascota(Mascota mascota);
    public void actualizarMascota(Mascota mascota);
    public List<Mascota> buscarMascotaEnRevision();
    public void aprobarMascotaDonante(Long mascotaId);
    public void rechazarMascotaDonante(Long mascotaId);
}
