package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Entrega;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.TurnoTransfusion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;



import com.tallerwebi.dominio.servicio.ServicioTurnoTransfusion;

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
    final private ServicioTurnoTransfusion servicioTurnoTransfusion;


    public ControladorHome(ServicioPublicacion servicioPublicacion, ServicioMascota servicioMascota, ServicioTurnoTransfusion servicioTurnoTransfusion) {
        this.servicioPublicacion = servicioPublicacion;
        this.servicioMascota = servicioMascota;
        this.servicioTurnoTransfusion = servicioTurnoTransfusion;

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
        List<Entrega> entregas = servicioPublicacion.obtenerEntregasXUsuarioId(usuarioEnSesion.getId());
        List<Publicacion> publicaciones = servicioPublicacion.obtenerTodasLasPublicaciones();
        List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno(usuarioEnSesion);
        List<TurnoTransfusion> turnosVet = servicioTurnoTransfusion.traerTurnosVigentesVet(usuarioEnSesion.getId());
        List<TurnoTransfusion> turnosReceptor = servicioTurnoTransfusion.traerTurnosVigentesReceptor(usuarioEnSesion.getId());
        List<TurnoTransfusion> turnosDonante = servicioTurnoTransfusion.traerTurnosVigentesDonante(usuarioEnSesion.getId());
        List<Mascota> mascotasNecesitadas = misMascotas.stream()
                .filter(Mascota::isReceptor)
                .filter(Mascota::isAprobado)
                .collect(Collectors.toList());

        model.addAttribute("entregas",entregas);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("mascotasNecesitadas", mascotasNecesitadas);
        model.addAttribute("turnosVet", turnosVet);
        model.addAttribute("turnosReceptor", turnosReceptor);
        model.addAttribute("turnosDonante", turnosDonante);

        return new ModelAndView("home", model);
    }
}