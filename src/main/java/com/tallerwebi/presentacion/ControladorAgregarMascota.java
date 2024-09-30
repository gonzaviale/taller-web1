package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorAgregarMascota {
    private ModelMap modelo = new ModelMap();
    private ServicioMascota servicioMascota;

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
    }


    @RequestMapping(path = "/agregar-donante", method = RequestMethod.POST)
    public ModelAndView agregarDonante(
            @RequestParam String nombre,
            @RequestParam Integer anios,
            @RequestParam Float peso,
            @RequestParam String tipo,
            HttpServletRequest request) {

        Mascota mascota;
        DuenoMascota duenoMascota = (DuenoMascota)request.getSession().getAttribute("usuarioEnSesion");

        if ("Felino".equalsIgnoreCase(tipo)) {
            mascota = new Felino();
        } else if ("Canino".equalsIgnoreCase(tipo)) {
            mascota = new Canino();
        } else {
            return new ModelAndView("error").addObject("message", "Tipo de mascota no válido");
        }

        mascota.setNombre(nombre);
        mascota.setAnios(anios);
        mascota.setPeso(peso);
        mascota.setDuenio(duenoMascota);
        mascota.setDonante(true);
        servicioMascota.registrarMascota(mascota);

        return new ModelAndView("home");
    }

}
