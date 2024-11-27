package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.servicio.ServicioPDFFile;
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
    private final ServicioPDFFile servicioPDFFile;

    @Autowired
    public ControladorAdministrador(ServicioFiltro servicioFiltro, ServicioPDFFile servicioPDFFile){
        this.servicioFiltro =servicioFiltro;
        this.servicioPDFFile= servicioPDFFile;
    }

    @RequestMapping(path = "/administrador")
    public ModelAndView irAHomeAdministrador(@RequestParam(name = "mensaje", required = false) String mensaje,
                                             HttpServletRequest request){
        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("administrador");

        if(usuarioEnSesion==null || !usuarioEnSesion.getRol().equals("administrador")){

            return new ModelAndView("redirect:/home");
        }

        ModelMap model= new ModelMap();

        List<Usuario> usuarios=servicioFiltro.obtenerTodosLosVeterinariosNoVerificados();
        List<Banco> bancos=servicioFiltro.obtenerTodosLosBancosNoVerificados();

        model.addAttribute("usuarios",usuarios);
        model.addAttribute("bancos",bancos);
        model.addAttribute("mensaje",mensaje);

        return new ModelAndView("administrador", model);
    }

    @RequestMapping("/aceptarUsuario")
    public ModelAndView aceptarUsuario(@RequestParam("id") Long id,
                                       HttpServletRequest request) {

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("administrador");

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

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("administrador");

        if (usuarioEnSesion == null || !usuarioEnSesion.getRol().equals("administrador")){
            return new ModelAndView("redirect:/home");
        }

        if(servicioFiltro.desactivarUsuarioBuscadoPor(id)){

            return new ModelAndView("redirect:/administrador?mensaje=usuario-inactivo");
        }

        return new ModelAndView("redirect:/administrador?mensaje=error");
    }

    @RequestMapping("/descargarMatricula")
    public ModelAndView descargarPdf(@RequestParam("id") Long id,
                                     HttpServletRequest request){

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("administrador");

        if (usuarioEnSesion == null || !usuarioEnSesion.getRol().equals("administrador")){
            return new ModelAndView("redirect:/home");
        }

        String nombre=servicioPDFFile.obtenerNombreArchivoPorPrefijo ("v_" + id);

        if(nombre!=null){

            return new ModelAndView("redirect:/downloadPdf?fileName=" + nombre);
        }

        return new ModelAndView("redirect:/administrador?mensaje=error descarga");
    }

}
