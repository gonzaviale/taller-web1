package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Score;
import com.tallerwebi.dominio.ServicioScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
        ArrayList<Score> scoreList = servicioScore.obtenerScoring();
        return new ModelAndView("scoring", "scoreList", scoreList);
    }
}
