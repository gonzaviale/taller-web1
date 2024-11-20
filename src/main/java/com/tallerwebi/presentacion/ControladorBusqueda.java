package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import com.tallerwebi.presentacion.DTO.UsuarioFiltradoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorBusqueda {

    private final ServicioFiltro servicioFiltro;
    private final ServicioMascota servicioMascota;

    @Autowired
    public ControladorBusqueda(ServicioFiltro serviciofiltro, ServicioMascota servicioMascota){
        this.servicioFiltro= serviciofiltro;
        this.servicioMascota = servicioMascota;
    }
    @RequestMapping(path = "/busquedaFiltrada")
    public ModelAndView buscar (@RequestParam("tipoDeBusqueda") String tipoDeBusqueda, HttpServletRequest request)
    {
        ModelMap model= new ModelMap();

        if(tipoDeBusqueda.equals("publicacion")){
            List<Publicacion> publicaciones= this.servicioFiltro.consultarPublicaciones("","","","");

            List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno((Usuario) request.getSession().getAttribute("usuarioEnSesion"));
            List<Mascota> mascotasNecesitadas = misMascotas.stream()
                    .filter(Mascota::isReceptor)
                    .filter(Mascota::isAprobado)
                    .collect(Collectors.toList());
            model.put("publicaciones",publicaciones);
            model.put("mascotasNecesitadas", mascotasNecesitadas);
        }
        if(tipoDeBusqueda.equals("banco de sangre")){
            List<BancoConTiposDeSangreDTO> bancoConTiposDeSangres= this.servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");
            model.put("listaBancos",bancoConTiposDeSangres);
        }
        if(tipoDeBusqueda.equals("usuarios")){
            List<Usuario> usuarios= this.servicioFiltro.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();
            model.put("listaUsuarios",usuarios);
        }
        if(tipoDeBusqueda.equals("veterinarios")){
            List<Usuario> usuarios= this.servicioFiltro.obtenerTodosLosVeterinariosVerificados();
            model.put("listaVeterinario",usuarios);
        }

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busqueda")
    public ModelAndView busqueda(){
        ModelMap model= new ModelMap();
        model.put("datos",new BancoConTiposDeSangreDTO());
        return new ModelAndView("busqueda",model);
    }

    @RequestMapping(path = "/busquedaFiltradaMascotas", method = RequestMethod.GET)
    public ModelAndView filtrarMascotas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String tipo)
    {
        ModelMap model = new ModelMap();

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota(nombre,sangre,tipo);
        model.put("listaMascotas",mascotas);

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busquedaFiltradaPublicaciones", method = RequestMethod.GET)
    public ModelAndView filtrarPublicaciones(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String zonaDeResidencia,
            @RequestParam(required = false) String tipoDePublicacion) {
        ModelMap model = new ModelMap();

        List<Publicacion> publicaciones = this.servicioFiltro.consultarPublicaciones(titulo, sangre, zonaDeResidencia, tipoDePublicacion);
        model.put("publicaciones", publicaciones);

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busquedaFiltradaBancos", method = RequestMethod.GET)
    public ModelAndView filtrarBancos(
            @RequestParam(required = false) String sangreBuscada,
            @RequestParam(required = false) String tipoDeProducto) {
        ModelMap model = new ModelMap();

        List<BancoConTiposDeSangreDTO> bancoConTiposDeSangres= this.servicioFiltro.obtenerCoincidenciasEnBancosDeSangre(sangreBuscada,tipoDeProducto);
        model.put("listaBancos",bancoConTiposDeSangres);

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/filtradodeusuarios", method = RequestMethod.GET)
    public ModelAndView filtradoDeUsuarios(
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String tipoDeBusqueda) {
        ModelMap model = new ModelMap();

        List<UsuarioFiltradoDTO> usuarios= this.servicioFiltro.obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(sangre,tipoDeBusqueda);
        model.put("listaUsuarios",usuarios);
        model.put("users","sad");

        return new ModelAndView("busqueda", model);
    }

}
