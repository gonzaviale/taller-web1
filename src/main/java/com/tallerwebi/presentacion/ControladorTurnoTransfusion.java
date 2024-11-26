package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.TurnoTransfusion;
import com.tallerwebi.dominio.servicio.ServicioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.servicio.ServicioTurnoTransfusion;
import com.tallerwebi.dominio.servicio.ServicioTurnoTransfusionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class ControladorTurnoTransfusion {
    private final ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion;
    private final ServicioTurnoTransfusion servicioTurnoTransfusion;

    @Autowired
    public ControladorTurnoTransfusion(ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion, ServicioTurnoTransfusion servicioTurnoTransfusion) {
        this.servicioSolicitudAUnaPublicacion = servicioSolicitudAUnaPublicacion;
        this.servicioTurnoTransfusion = servicioTurnoTransfusion;
    }

    @RequestMapping(path = "/crear-turno", method = RequestMethod.POST)
    public ModelAndView crearTurno(@RequestParam("fechaYHora") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaYHora,
                                   @RequestParam("solicitudId") Long solicitudId) {
        TurnoTransfusion turno = new TurnoTransfusion();
        turno.setFechaYHora(fechaYHora);
        turno.setSolicitudAUnaPublicacion(servicioSolicitudAUnaPublicacion.traerSolicitudPorId(solicitudId));
        servicioTurnoTransfusion.guardarTurno(turno);
        return new ModelAndView("redirect:/miPerfil");
    }
}