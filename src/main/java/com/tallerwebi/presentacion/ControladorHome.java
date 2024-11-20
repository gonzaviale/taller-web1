package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorHome {

    final private ServicioPublicacion servicioPublicacion;
    final private ServicioMascota servicioMascota;

    public ControladorHome(ServicioPublicacion servicioPublicacion, ServicioMascota servicioMascota) {
        this.servicioPublicacion = servicioPublicacion;
        this.servicioMascota = servicioMascota;
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
            model.addAttribute("rol", usuarioEnSesion.getRol());
        }

        List<Publicacion> publicaciones = servicioPublicacion.obtenerTodasLasPublicaciones();
        List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno(usuarioEnSesion);
        List<Mascota> mascotasNecesitadas = misMascotas.stream()
                .filter(Mascota::isReceptor)
                .collect(Collectors.toList());
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("mascotasNecesitadas", mascotasNecesitadas);

        return new ModelAndView("home", model);
    }
}