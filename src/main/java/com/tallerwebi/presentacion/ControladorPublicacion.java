package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioMascotaImpl;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorPublicacion {

    final private ServicioPublicacion servicioPublicacion;
    final private ServicioMascota servicioMascota;

    @Autowired
    public ControladorPublicacion(ServicioPublicacion servicioPublicacion, ServicioMascota servicioMascota) {
        this.servicioPublicacion = servicioPublicacion;
        this.servicioMascota = servicioMascota;
    }

    @RequestMapping(path = "/publicarPublicacion", method = RequestMethod.POST)
    public ModelAndView publicarPublicacion(@ModelAttribute("publicacion") Publicacion nuevaPublicacion, @RequestParam("mascota") Long mascotaId, HttpServletRequest request) {
        ModelMap model;

        if (request != null && request.getSession().getAttribute("usuarioEnSesion") != null) {
            nuevaPublicacion.setDuenioPublicacion((Usuario) request.getSession().getAttribute("usuarioEnSesion"));
        }
        Mascota mascota = servicioMascota.buscarMascotaPorId(mascotaId);
        nuevaPublicacion.setMascotaDonante(mascota);
        try {
            servicioPublicacion.guardarPublicacion(nuevaPublicacion);
        } catch (PublicacionSinTipoDeSangre ex) {
            model = new ModelMap("mensaje", "Publicacion no registrada: el campo sangre no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        } catch (PublicacionSinTipoDePublicacion e) {
            model = new ModelMap("mensaje", "Publicacion no registrada: el campo tipo de publicacion no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        } catch (PublicacionNoValida e) {
            model = new ModelMap("mensaje", "Publicacion no registrada: el campo tipo de publicacion y el campo de sangre no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        } catch (PublicacionSinTitulo e) {
            model = new ModelMap("mensaje", "Publicacion no registrada: el campo titulo de la publicacion no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        }
        model = new ModelMap("mensaje", "la publicacion fue registrada correctamente");

        return new ModelAndView("redirect:/home", model);
    }

    @RequestMapping(path = "/crear-publicacion")
    public ModelAndView nuevaPublicacion(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno((Usuario) request.getSession().getAttribute("usuarioEnSesion"));
        List<Mascota> mascotasDonantes = misMascotas.stream()
                .filter(Mascota::isDonante)
                .filter(Mascota::isAprobado)
                .collect(Collectors.toList());
        model.put("publicacion", new Publicacion());
        model.put("misMascotas", mascotasDonantes);

        return new ModelAndView("crear-publicacion", model);
    }

}
