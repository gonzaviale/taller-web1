package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorAdministrador {

    private final ServicioFiltro servicioFiltro;

    @Autowired
    public ControladorAdministrador(ServicioFiltro servicioFiltro){
        this.servicioFiltro =servicioFiltro;
    }

    @RequestMapping(path = "/administrador")
    public ModelAndView irAHomeAdministrador(@RequestParam(name = "mensaje", required = false) String mensaje,
                                             HttpServletRequest request){
        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if(usuarioEnSesion==null || !usuarioEnSesion.getRol().equals("administrador")){

            return new ModelAndView("redirect:/home");
        }

        ModelMap model= new ModelMap();

        List<Usuario> usuarios=servicioFiltro.obtenerTodosLosVeterinariosNoVerificados();

        model.addAttribute("usuarios",usuarios);
        model.addAttribute("mensaje",mensaje);

        return new ModelAndView("administrador", model);
    }

    @RequestMapping("/aceptarUsuario")
    public ModelAndView aceptarUsuario(@RequestParam("id") Long id,
                                       HttpServletRequest request) {

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (usuarioEnSesion == null || !usuarioEnSesion.getRol().equals("administrador")){
            return new ModelAndView("redirect:/home");
        }

        if(servicioFiltro.activarUsuarioBuscadoPor(id)){

            return new ModelAndView("redirect:/administrador?mensaje=usuario-activado");
        }

        return new ModelAndView("redirect:/administrador?mensaje=error");
    }

    @RequestMapping("/rechazarUsuario")
    public ModelAndView rechazarUsuario(@RequestParam("id") Long id,
                                       HttpServletRequest request) {

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (usuarioEnSesion == null || !usuarioEnSesion.getRol().equals("administrador")){
            return new ModelAndView("redirect:/home");
        }

        if(servicioFiltro.desactivarUsuarioBuscadoPor(id)){

            return new ModelAndView("redirect:/administrador?mensaje=usuario-inactivo");
        }

        return new ModelAndView("redirect:/administrador?mensaje=error");
    }

}
