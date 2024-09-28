package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DuenoMascota;
import com.tallerwebi.dominio.Mascota;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAgregarMascota {
    private ModelMap modelo = new ModelMap();

    @RequestMapping(path = "/agregar-donante", method = RequestMethod.POST)
    public ModelAndView agregarDonante(Mascota mascota, DuenoMascota duenoMascota) {
        mascota.setDuenio(duenoMascota);

        if (!mascota.isReceptor()) {
            mascota.setDonante(true);
        }
        return new ModelAndView("home");
    }
}
