package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioFiltro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorBusqueda {

    private final ServicioFiltro servicioFiltro;

    @Autowired
    public ControladorBusqueda(ServicioFiltro serviciofiltro){
        this.servicioFiltro= serviciofiltro;
    }
    @RequestMapping(path = "/busquedaFiltrada")
    public ModelAndView buscar (@RequestParam("tipoDeBusqueda") String tipoDeBusqueda,
                                @RequestParam ("search") String search)
    {

        ModelMap model= new ModelMap();

        if(tipoDeBusqueda.equals("mascotas")){
            List<Mascota> mascotas= this.servicioFiltro.consultarMascota("",search ,"");
            model.put("listaMascotas",mascotas);
        }
        if(tipoDeBusqueda.equals("publicacion")){
            List<Publicacion> publicaciones= this.servicioFiltro.consultarPublicaciones("",search,"","");
            model.put("publicaciones",publicaciones);
        }

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busqueda")
    public ModelAndView busqueda(){
        ModelMap model= new ModelMap();
        model.put("datos",new datosBusqueda());
        return new ModelAndView("busqueda",model);
    }

}
