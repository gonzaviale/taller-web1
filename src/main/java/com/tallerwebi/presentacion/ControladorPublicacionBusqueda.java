package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPublicacionBusqueda {

    final private ServicioPublicacion servicioPublicacion;

    @Autowired
    public ControladorPublicacionBusqueda(ServicioPublicacion servicioPublicacion){
        this.servicioPublicacion = servicioPublicacion;
    }

    public ModelAndView publicarPublicacion(String campoDeSangre) {
        ModelMap model;
        if (campoDeSangre.isEmpty()) {
            model = new ModelMap("error", "el campo sangre no puede estar vacio");
            return new ModelAndView("error", model);
        }
        //realizaria la publicacion
        Publicacion publicacion = new Publicacion();
        publicacion.setTipoDeSangre(campoDeSangre);
        try {
            servicioPublicacion.guardarPublicacion(publicacion);
        }catch (PublicacionSinTipoDeSangre ex){
            model = new ModelMap("error", "el campo sangre no puede estar vacio");
            return new ModelAndView("error", model);
        }
        model = new ModelMap("registro exitoso", "la publicacion fue registrada correctamente");
        return new ModelAndView("redirect:/registro exitoso", model);
    }


}
