package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campania;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @RequestMapping("/crearCampania")
    public ModelAndView crearCampania(HttpSession session,
                                      @RequestParam(value = "error", required = false) String error) {


        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();
        Campania campania = new Campania();
        modelo.addAttribute("campana", campania);
        return new ModelAndView("crearCampania", modelo);
    }
}