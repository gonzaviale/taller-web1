package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class ControladorFiltro {
    private ServicioFiltro servicioFiltro;
    @Autowired
    public ControladorFiltro(ServicioFiltro servicioFiltro) {
        this.servicioFiltro = servicioFiltro;
    }

    @RequestMapping(path = "/filtro", method = RequestMethod.GET)
    public ModelAndView filtrarMascotas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String tipo)
    {
        ModelMap model = new ModelMap();
        ArrayList<Mascota> mascotas = new ArrayList<>();

        mascotas = servicioFiltro.consultarMascota(
                nombre != null ? nombre : "",
                sangre != null ? sangre : "",
                tipo != null ? tipo : ""
        );
        model.put("listaMascotas",mascotas);

        return new ModelAndView("filtrar", model);
    }

    @RequestMapping(path = "/buscar")
    public ModelAndView buscarMascotas(){
        return new ModelAndView("buscar");
    }

}
