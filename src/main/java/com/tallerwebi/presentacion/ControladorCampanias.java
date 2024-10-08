package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorCampanias {

    private final ServicioBanco servicioBanco;

    @Autowired
    public ControladorCampanias(ServicioBanco servicioBanco) {
        this.servicioBanco = servicioBanco;
    }


    @RequestMapping(value = "/crearCampania", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioCrearCampania(HttpSession session,
                                                       @RequestParam(value = "error", required = false) String error) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();

        if (idBanco == null) {
            return new ModelAndView("redirect:/login");
        }

        modelo.put("error", error);
        modelo.put("campana", new Campana());
        return new ModelAndView("crearCampania", modelo);
    }

    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public ModelAndView crearCampania(@ModelAttribute("campana") Campana campana,
                                      HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");

        if (idBanco == null) {
            return new ModelAndView("redirect:/login");
        }


        servicioBanco.guardarCampania(campana);

        return new ModelAndView("redirect:/campanas");
    }
}




