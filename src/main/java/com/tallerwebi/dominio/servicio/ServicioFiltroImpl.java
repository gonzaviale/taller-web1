package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import com.tallerwebi.presentacion.DTO.UsuarioFiltradoDTO;
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
    public List<UsuarioFiltradoDTO> obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(String sangreBuscada, String tipoDeBusqueda) {

        sangreBuscada=validadorCampo(sangreBuscada);

        if(tipoDeBusqueda.equals("publicacion")){
            List<Usuario> usuarios= repositorioUsuario.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada);

            return this.asignarUsuariosDTOSConPublicaciones(usuarios,sangreBuscada);
        }
        if(tipoDeBusqueda.equals("mascota")){
            List<Usuario> usuarios= repositorioUsuario.obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada(sangreBuscada);

            return this.asignarUsuariosDTOSMascotas(usuarios,sangreBuscada);
        }

        return null;
    }

    private List<UsuarioFiltradoDTO> asignarUsuariosDTOSMascotas(List<Usuario> usuarios,String sangre) {
        List<UsuarioFiltradoDTO> filtradoDTOS= new ArrayList<>();

        for (Usuario usuario : usuarios){
            if (this.filtrarPorMascotaConTipoDeSangre(usuario.getMascotas(),sangre)!=0) {
                filtradoDTOS.add(new UsuarioFiltradoDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getMascotas().size()));
            }
        }

        return filtradoDTOS;

    }

    private long filtrarPorMascotaConTipoDeSangre(List<Mascota> mascotas, String sangre) {
        if (mascotas == null || sangre == null || sangre.isEmpty()) {
            return 0;
        }

        return mascotas.stream()
                .filter(mascota -> mascota.getSangre().equalsIgnoreCase(sangre))
                .count();
    }


    private List<UsuarioFiltradoDTO> asignarUsuariosDTOSConPublicaciones(List<Usuario> usuarios,String sangre) {

        List<UsuarioFiltradoDTO> filtradoDTOS= new ArrayList<>();

        for (Usuario usuario : usuarios) {
            Integer cantidadPublicacionesBusqueda = 0;
            Integer cantidadPublicacionesDonacion = 0;
            Integer cantidadPublicacionVenta = 0;
            cantidadPublicacionesBusqueda = contarPublicacionesPorTipo(usuario, "busqueda", sangre);
            cantidadPublicacionesDonacion = contarPublicacionesPorTipo(usuario, "donacion", sangre);
            cantidadPublicacionVenta = contarPublicacionesPorTipo(usuario, "venta", sangre);

            filtradoDTOS.add(new UsuarioFiltradoDTO(usuario.getId()
                    , usuario.getNombre(), usuario.getApellido()
                    , usuario.getEmail(), cantidadPublicacionesBusqueda, cantidadPublicacionesDonacion, cantidadPublicacionVenta));
        }

        return filtradoDTOS;
    }

    private Integer contarPublicacionesPorTipo(Usuario usuario, String tipo, String sangre) {

        Integer count = 0;

        for (int i = 0; i < usuario.getPublicaciones().size(); i++) {
            if (usuario.getPublicaciones().get(i).getTipoDePublicacion().equalsIgnoreCase(tipo) && usuario.getPublicaciones().get(i).getTipoDeSangre().equalsIgnoreCase(sangre)) {
                count++;
            }
        }

        return count;

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