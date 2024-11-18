package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("servicioFiltro")
@Transactional
public class ServicioFiltroImpl implements ServicioFiltro {

    private final RepositorioMascota repositorioMascota;
    private final RepositorioPublicacion repositorioPublicacion;
    private final RepositorioBanco repositorioBanco;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioFiltroImpl(RepositorioMascota repositorioMascota, RepositorioPublicacion repositorioPublicacion, RepositorioBanco repositorioBanco, RepositorioUsuario repositorioUsuario) {
        this.repositorioMascota = repositorioMascota;
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioBanco = repositorioBanco;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo) {

        nombre = validadorCampo(nombre);
        sangre = validadorCampo(sangre);
        tipo = validadorCampo(tipo);

        return new ArrayList<>(repositorioMascota.buscarMascota(nombre, sangre, tipo));
    }

    @Override
    public ArrayList<Publicacion> consultarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion) {

        titulo = validadorCampo(titulo);
        tipoDeSangre = validadorCampo(tipoDeSangre);
        zonaDeResidencia = validadorCampo(zonaDeResidencia);
        tipoDePublicacion = validadorCampo(tipoDePublicacion);

        return new ArrayList<>(repositorioPublicacion.buscarPublicaciones(titulo, tipoDeSangre, zonaDeResidencia, tipoDePublicacion));
    }

    @Override
    public ArrayList<BancoConTiposDeSangreDTO> obtenerCoincidenciasEnBancosDeSangre(String sangreBuscada, String tipoProducto) {
        ArrayList<BancoConTiposDeSangreDTO> coincidenciasEnBancos = new ArrayList<>();
        if (validadorCampo(sangreBuscada).equals(sangreBuscada) && validadorCampo(tipoProducto).isEmpty()) {
            coincidenciasEnBancos.addAll(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos(sangreBuscada));
            coincidenciasEnBancos.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
            return coincidenciasEnBancos;
        }
        if (validadorCampo(sangreBuscada).isEmpty() && validadorCampo(tipoProducto).equals(tipoProducto)) {
            coincidenciasEnBancos.addAll(repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos(tipoProducto));
            coincidenciasEnBancos.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
            return coincidenciasEnBancos;
        }
        if (validadorCampo(sangreBuscada).equals(sangreBuscada) && validadorCampo(tipoProducto).equals(tipoProducto)) {
            coincidenciasEnBancos.addAll(repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(sangreBuscada, tipoProducto));
            coincidenciasEnBancos.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
            return coincidenciasEnBancos;
        }
        coincidenciasEnBancos.addAll(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos(sangreBuscada));
        coincidenciasEnBancos.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
        return coincidenciasEnBancos;
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta() {
        return repositorioUsuario.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();
    }

    @Override
    public List<Usuario> obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(String sangreBuscada, String tipoDeBusqueda) {

        sangreBuscada=validadorCampo(sangreBuscada);

        if(tipoDeBusqueda.equals("publicacion")){
            return repositorioUsuario.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada);
        }
        if(tipoDeBusqueda.equals("mascota")){
            return repositorioUsuario.obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada(sangreBuscada);
        }

        return this.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();
    }

    @Override
    public List<Usuario> obtenerTodosLosVeterinariosVerificados() {
        return repositorioUsuario.obtenerTodosLosVeterinariosVerificados();
    }

    @Override
    public List<Usuario> obtenerTodosLosVeterinariosNoVerificados() {
        return repositorioUsuario.obtenerTodosLosVeterinariosNoVerificados();
    }

    @Override
    public boolean activarUsuarioBuscadoPor(Long id) {

        return repositorioUsuario.activarUsuarioBuscadoPor(id);
    }

    @Override
    public boolean desactivarUsuarioBuscadoPor(Long id) {
        return repositorioUsuario.desactivarUsuarioBuscadoPor(id);
    }

    private String validadorCampo(String campo) {
        return (campo == null || campo.isEmpty()) ? "" : campo;
    }

}