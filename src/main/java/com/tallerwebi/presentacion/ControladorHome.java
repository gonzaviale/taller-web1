package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.ServicioPublicacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorHome {

    final private ServicioPublicacion servicioPublicacion;

    public ControladorHome(ServicioPublicacion servicioPublicacion) {
        this.servicioPublicacion = servicioPublicacion;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome(
            @RequestParam(name = "mensaje", required = false) String mensaje,
            HttpServletRequest request,
            ModelMap model) {

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (usuarioEnSesion != null) {
            System.out.println("hola" + usuarioEnSesion.getNombre());
            // Crear el mensaje de bienvenida usando el nombre del usuario
            String mensajeBienvenida = "Bienvenido, " + usuarioEnSesion.getNombre();  // Asumo que tienes un getNombre() en Usuario
            model.addAttribute("mensajeBienvenida", mensajeBienvenida);
        }

        List<Publicacion> publicaciones = servicioPublicacion.obtenerTodasLasPublicaciones();
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("mensaje", mensaje);

        return new ModelAndView("home", model);
    }



}
