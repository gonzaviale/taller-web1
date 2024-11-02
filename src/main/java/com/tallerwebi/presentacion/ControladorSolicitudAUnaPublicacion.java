package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import com.tallerwebi.dominio.servicio.ServicioSolicitudAUnaPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorSolicitudAUnaPublicacion {
    final private ServicioMascota servicioMascota;
    final private ServicioSolicitudAUnaPublicacion servicioSolicitud;
    final private ServicioPublicacion servicioPublicacion;
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorSolicitudAUnaPublicacion(ServicioMascota servicioMascota, ServicioSolicitudAUnaPublicacion servicioSolicitud, ServicioPublicacion servicioPublicacion) {
        this.servicioMascota = servicioMascota;
        this.servicioSolicitud = servicioSolicitud;
        this.servicioPublicacion = servicioPublicacion;
    }

    @RequestMapping(path = "/realizar-solicitud", method = RequestMethod.POST)
    public ModelAndView realizarSolicitud(@RequestParam("mascotaDonante") Long mascotaDonanteId,
                                          @RequestParam("mascotaReceptora") Long mascotaReceptoraId,
                                          @RequestParam("publicacion") Long publicacionId) throws PublicacionNoExistente {

        SolicitudAUnaPublicacion solicitud = new SolicitudAUnaPublicacion();
        solicitud.setMascotaDonante(servicioMascota.buscarMascotaPorId(mascotaDonanteId));
        solicitud.setMascotaReceptora(servicioMascota.buscarMascotaPorId(mascotaReceptoraId));
        solicitud.setPublicacion(servicioPublicacion.busquedaPorId(publicacionId));
        solicitud.setAprobada(false);
        solicitud.setPendiente(true);
        solicitud.setRechazada(false);
        solicitud.setVista(false);

        servicioSolicitud.guardarSolicitud(solicitud);

        servicioPublicacion.desactivarPublicacion(publicacionId);
        modelo.put("solicitudPubliExitosa", "Tu solicitud ya fue enviada");
        return new ModelAndView("redirect:/home", modelo);
    }

    @RequestMapping(path = "/aceptar-solicitud-publicacion", method = RequestMethod.POST)
    public ModelAndView aceptarSolicitud(@RequestParam("solicitudId") Long solicitud){
        servicioSolicitud.aceptarSolicitud(solicitud);

        return new ModelAndView("redirect:/miPerfil");
    }

    @RequestMapping(path = "/rechazar-solicitud-publicacion", method = RequestMethod.POST)
    public ModelAndView rechazarSolicitud(@RequestParam("solicitudId") Long solicitud){
        servicioSolicitud.rechazarSolicitud(solicitud);
        servicioPublicacion.activarPublicacion(servicioSolicitud.traerSolicitudPorId(solicitud).getPublicacion().getId());
        return new ModelAndView("redirect:/miPerfil");
    }

    @RequestMapping(path = "/solicitud-vista", method = RequestMethod.POST)
    public ModelAndView solicitudVista(@RequestParam("solicitudId") Long solicitud){
        servicioSolicitud.marcarComoVista(solicitud);
        return new ModelAndView("redirect:/miPerfil");
    }
}
