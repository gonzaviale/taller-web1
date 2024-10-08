package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorMensajeUsuarioBanco {

    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco;

    @Autowired
    public ControladorMensajeUsuarioBanco(ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco) {
        this.servicioMensajeUsuarioBanco = servicioMensajeUsuarioBanco;
    }

    @RequestMapping(path = "/enviarMensajeUsuarioBanco", method = RequestMethod.POST)
    public ModelAndView enviarMensajeUsuarioBanco(@ModelAttribute("mensajeUsuarioBanco") String mensajeUsuarioBanco) {
        ModelMap model = new ModelMap();
        model.put("mensajeUsuarioBanco", "Mensaje enviado al banco con exito");
        model.put("publicaciones", "");
        return new ModelAndView("home", model);
    }
}
