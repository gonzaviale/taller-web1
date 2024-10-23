package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.servicio.ServicioSolicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorSolicitudes {

    private final ServicioSolicitud servicioSolicitud;

    @Autowired
    public ControladorSolicitudes(ServicioSolicitud servicioSolicitud) {
            this.servicioSolicitud = servicioSolicitud;
    }



    @RequestMapping("/verPeticiones")
    public ModelAndView BancoVerPeticiones(HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");
        }
        Long idBanco = (Long) session.getAttribute("idBanco");

        ModelMap modelo = new ModelMap();

        List<Solicitud> solicitudes = servicioSolicitud.obtenerSolicitudesXBanco(idBanco);
        modelo.addAttribute("solicitudes", solicitudes);
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("bancoVerPeticiones", modelo);
    }


    @RequestMapping("/revisarSolicitud")
    public ModelAndView BancoVerPeticion(@RequestParam("solicitudId") int solicitudId, HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");
        }
        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();
        Solicitud solicitud = servicioSolicitud.buscarSolicitud(solicitudId);
        List<PaqueteDeSangre> paquetes = servicioSolicitud.obtenerPaquetesDeSangreCompatibles(solicitud);

        modelo.addAttribute("solicitud", solicitud);
        modelo.addAttribute("paquetes", paquetes);
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("bancoVerSolicitud", modelo);
    }


    @RequestMapping(value = "/rechazarSolicitud", method = RequestMethod.POST)
    public String rechazarSolicitud(@RequestParam("solicitudId") int solicitudId, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }

        servicioSolicitud.rechazarSolicitud(solicitudId);

        return "redirect:/verPeticiones";

    }

    @RequestMapping(value = "/asignarPaquete", method = RequestMethod.POST)
    public String asignarPaquete(@RequestParam("solicitudId") int solicitudId,
                                 @RequestParam("paqueteId") int paqueteId,
                                 HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);

        return "redirect:/verPeticiones";
    }


    private boolean verificarSesion(HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        return idBanco != null;
    }


}
