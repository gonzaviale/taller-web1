package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Override
    public Boolean eliminarMascota(Mascota mascotaBuscada) {

        return repositorioMascota.eliminarMascota(mascotaBuscada);
    }

    @Override
    public List<String> obtenerSangreSegunTipoDeMascota(String tipo) {

        if(tipo.equalsIgnoreCase("Canino")){
            return this.obtenerListaDeSangreDeCanino();
        }

        return this.obtenerListaDeSangreDeFelino();
    }

    @Override
    public void editarMascota(Mascota mascotaBuscada) {
        repositorioMascota.actualizarMascota(mascotaBuscada);
    }

    @Override
    public boolean isEdadApropiadaDonante(Mascota mascota) {
        if (isValid(mascota)) {
            return mascota.getAnios() >= 1 && mascota.getAnios() <= 8; // Edad entre 1 y 8 años inclusivos
        }
        return false;
    }

    @Override
    public boolean isPesoCorrectoCanino(Mascota mascota) {
        if (isValid(mascota)) {
            return mascota.getPeso() >= 25f; // El peso debe ser mayor o igual a 25 kg
        }
        return false;
    }

    @Override
    public boolean isPesoCorrectoFelino(Mascota mascota) {
        if (isValid(mascota) ) {
            return mascota.getPeso() >= 3.5f; // El peso debe ser mayor o igual a 3.5 kg
        }
        return false;
    }

    private static boolean isValid(Mascota mascota) {
        return mascota != null &&
                mascota.getTipo() != null &&
                mascota.getPeso() != null &&
                mascota.getAnios() != null &&
                mascota.isDonante();
    }


    private List<String> obtenerListaDeSangreDeCanino() {
        List<String> tiposDeSangre = new ArrayList<>();
        tiposDeSangre.add("DEA-1.1.");
        tiposDeSangre.add("DEA-1.2.");
        tiposDeSangre.add("DEA-3");
        tiposDeSangre.add("DEA-4");
        tiposDeSangre.add("DEA-5");
        tiposDeSangre.add("DEA-6");
        tiposDeSangre.add("DEA-7");
        tiposDeSangre.add("DEA-8");

        return tiposDeSangre;
    }

    private List<String> obtenerListaDeSangreDeFelino(){
        List<String> tiposDeSangre = new ArrayList<>();
        tiposDeSangre.add("A");
        tiposDeSangre.add("B");
        tiposDeSangre.add("AB");

        return tiposDeSangre;
    }

}
