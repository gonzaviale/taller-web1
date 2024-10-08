package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPerfil {

    private final ServicioPerfil servicioPerfil;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil) {
        this.servicioPerfil = servicioPerfil;
    }

    @RequestMapping("/miPerfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request) {

        ModelMap model = new ModelMap();
        Usuario usuarioBuscado=null;
        Usuario usuarioEnSesion= (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        if(usuarioEnSesion!=null){
            usuarioBuscado = servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId());
        }

        if (usuarioBuscado != null) {
            model.addAttribute("usuarioBuscado", usuarioBuscado);
            return new ModelAndView("perfil", model);
        }

        return new ModelAndView("redirect:home");
    }

    @RequestMapping("/perfil{id}")
    public ModelAndView mostrarPerfil(@PathVariable Long id) {

        Usuario usuarioBuscado = servicioPerfil.buscarUsuarioPorId(id);
        ModelMap model= new ModelMap();
        if (usuarioBuscado != null) {
            model.addAttribute("usuarioBuscado", usuarioBuscado);
            return new ModelAndView("perfil", model);
        }
        return new ModelAndView("redirect:home");
    }

}
