package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Score;
import com.tallerwebi.dominio.servicio.ServicioScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class ControladorScoring {

    private ServicioScore servicioScore;

    @Autowired
    public ControladorScoring(ServicioScore servicioScore) {
        this.servicioScore = servicioScore;
    }

    @RequestMapping(path = "/scoring")
    public ModelAndView scoring() {
        return new ModelAndView("scoring");
    }

    @RequestMapping(path="/filtrarScoring", method = RequestMethod.POST)
    public ModelAndView filtrarScoring(@ModelAttribute("sangre") String sangre) {
        ArrayList<Banco> scoring = servicioScore.obtenerScoring(sangre);
        ModelMap model = new ModelMap();
        model.put("scoring", scoring);
        return new ModelAndView("scoring", model);
    }
}
