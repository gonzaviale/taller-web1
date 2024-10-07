package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorBusqueda {

    private final ServicioFiltro servicioFiltro;

    @Autowired
    public ControladorBusqueda(ServicioFiltro serviciofiltro){
        this.servicioFiltro= serviciofiltro;
    }
    @RequestMapping(path = "/busquedaFiltrada")
    public ModelAndView buscar (@RequestParam("tipoDeBusqueda") String tipoDeBusqueda)
    {
        ModelMap model= new ModelMap();

        if(tipoDeBusqueda.equals("mascotas")){
            List<Mascota> mascotas= this.servicioFiltro.consultarMascota("","" ,"");
            model.put("listaMascotas",mascotas);
        }
        if(tipoDeBusqueda.equals("publicacion")){
            List<Publicacion> publicaciones= this.servicioFiltro.consultarPublicaciones("","","","");
            model.put("publicaciones",publicaciones);
        }
        if(tipoDeBusqueda.equals("banco de sangre")){
            List<BancoConTiposDeSangre> bancoConTiposDeSangres= this.servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");
            model.put("listaBancos",bancoConTiposDeSangres);
        }

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busqueda")
    public ModelAndView busqueda(){
        ModelMap model= new ModelMap();
        model.put("datos",new BancoConTiposDeSangre());
        return new ModelAndView("busqueda",model);
    }

    @RequestMapping(path = "/busquedaFiltradaMascotas", method = RequestMethod.GET)
    public ModelAndView filtrarMascotas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String tipo)
    {
        ModelMap model = new ModelMap();

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota(nombre,sangre,tipo);
        model.put("listaMascotas",mascotas);

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busquedaFiltradaPublicaciones", method = RequestMethod.GET)
    public ModelAndView filtrarPublicaciones(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String sangre,
            @RequestParam(required = false) String zonaDeResidencia,
            @RequestParam(required = false) String tipoDePublicacion) {
        ModelMap model = new ModelMap();

        List<Publicacion> publicaciones = this.servicioFiltro.consultarPublicaciones(titulo, sangre, zonaDeResidencia, tipoDePublicacion);
        model.put("publicaciones", publicaciones);

        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/busquedaFiltradaBancos", method = RequestMethod.GET)
    public ModelAndView filtrarBancos(
            @RequestParam(required = false) String sangreBuscada,
            @RequestParam(required = false) String tipoDeProducto) {
        ModelMap model = new ModelMap();

        List<BancoConTiposDeSangre> bancoConTiposDeSangres= this.servicioFiltro.obtenerCoincidenciasEnBancosDeSangre(sangreBuscada,tipoDeProducto);
        model.put("listaBancos",bancoConTiposDeSangres);

        return new ModelAndView("busqueda", model);
    }


}
