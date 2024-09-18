package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPublicacion {

    final private ServicioPublicacion servicioPublicacion;

    @Autowired
    public ControladorPublicacion(ServicioPublicacion servicioPublicacion){
        this.servicioPublicacion = servicioPublicacion;
    }
    @RequestMapping(path ="/publicarPublicacion", method = RequestMethod.POST)
    public ModelAndView publicarPublicacion(@ModelAttribute("publicacion") Publicacion nuevaPublicacion) {
        ModelMap model;
        try {
            servicioPublicacion.guardarPublicacion(nuevaPublicacion);
        }catch (PublicacionSinTipoDeSangre ex){
            model = new ModelMap("error", "el campo sangre no puede estar vacio");
            return new ModelAndView("error", model);
        } catch (PublicacionSinTipoDePublicacion e) {
            model = new ModelMap("error", "el campo tipo de publicacion no puede estar vacio");
            return new ModelAndView("error", model);
        }catch (PublicacionNoValida e) {
            model = new ModelMap("error", "el campo tipo de publicacion y el campo de sangre no puede estar vacio");
            return new ModelAndView("error", model);
        }
        model = new ModelMap("registroExitoso", "la publicacion fue registrada correctamente");
        return new ModelAndView("redirect:/registro-exitoso", model);
    }

    @RequestMapping(path ="/crear-publicacion")
    public ModelAndView nuevaPublicacion() {
        ModelMap model= new ModelMap();
        model.put("publicacion", new Publicacion());
        return new ModelAndView("crear-publicacion",model);
    }

    @RequestMapping("/registro-exitoso")
    public ModelAndView registroExitoso() {
        return new ModelAndView("registro-exitoso");
    }

}
