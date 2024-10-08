package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
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
        modelo.put("campana", new CampanaDTO());
        return new ModelAndView("crearCampania", modelo);
    }

    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public ModelAndView crearCampania(@ModelAttribute("campana") Campana campanaDTO,
                                      HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        if (idBanco == null) {
            return new ModelAndView("redirect:/login");
        }
        Banco banco = servicioBanco.BuscarBancoId(idBanco);
       CampanaDTO campanaEntidad = new CampanaDTO();
        campanaEntidad.setNombre(campanaDTO.getNombre());
        campanaEntidad.setFechaInicio(campanaDTO.getFechaInicio());
        campanaEntidad.setFechaFin(campanaDTO.getFechaFin());
        campanaEntidad.setUbicacion(campanaDTO.getUbicacion());
        campanaEntidad.setDescripcion(campanaDTO.getDescripcion());
        campanaEntidad.setBanco(banco);

        servicioBanco.guardarCampania(campanaEntidad);

        return new ModelAndView("redirect:/bancoHome");
    }
}




