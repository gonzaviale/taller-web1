package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.servicio.ServicioMascota;
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
        ModelMap model = new ModelMap();
        model.put("publicaciones", "");

        if (request != null && request.getSession().getAttribute("usuarioEnSesion") != null) {
            nuevaPublicacion.setDuenioPublicacion((Usuario) request.getSession().getAttribute("usuarioEnSesion"));
        }
        if (mascotaId == 0 || mascotaId == null) {
            model.put("mensaje", "Debe ingresar una mascota");
            return new ModelAndView("/crear-publicacion", model);
        } else {
            Mascota mascota = servicioMascota.buscarMascotaPorId(mascotaId);
            nuevaPublicacion.setMascotaDonante(mascota);
            nuevaPublicacion.setTipoDeSangre(mascota.getSangre());
        }
        try {
            servicioPublicacion.guardarPublicacion(nuevaPublicacion);
        } catch (PublicacionNoValida e) {
            model.put("mensaje", "Publicacion no registrada: el campo tipo de publicacion y el campo de sangre no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        } catch (PublicacionSinTitulo e) {
            model.put("mensaje", "Publicacion no registrada: el campo titulo de la publicacion no puede estar vacio");
            return new ModelAndView("redirect:/home", model);
        }
        model.put("mensaje", "la publicacion fue registrada correctamente");
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

    @RequestMapping(path = "/desactivar-publicacion")
    public ModelAndView desactivarPublicacion(HttpServletRequest request, @RequestParam(value = "id", required = false) Long id) throws PublicacionNoExistente {

        ModelMap model= new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {
            model.put("mensaje","accion invalida o no esta logeado o no es su publicacion");
            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        if(servicioPublicacion.busquedaPorId(id).getDuenioPublicacion().getId().equals(idUsuarioEnSesion)){

            servicioPublicacion.desactivarPublicacion(id);

            model.put("mensaje","se desactivo correctamente su publicacion no aparecera en las busquedas pero seguira siendo accesible desde su perfil");
        }else{

            model.put("mensaje","accion invalida no es su publicacion");
        }

        return new ModelAndView("redirect:/home", model);
    }

    @RequestMapping(path = "/activar-publicacion")
    public ModelAndView activarPublicacion(HttpServletRequest request, @RequestParam(value = "id", required = false) Long id) throws PublicacionNoExistente {

        ModelMap model= new ModelMap();


        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {
            model.put("mensaje","accion invalida o no esta logeado o no es su publicacion");
            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        if(servicioPublicacion.busquedaPorId(id).getDuenioPublicacion().getId().equals(idUsuarioEnSesion)){

            servicioPublicacion.activarPublicacion(id);

            model.put("mensaje","se activo correctamente su publicacion aparecera en las busquedas a partir de ahora");
        }else{

            model.put("mensaje","accion invalida no es su publicacion");
        }


        return new ModelAndView("redirect:/home", model);

    }

    @RequestMapping(path = "/actualizar-publicacion")
    public ModelAndView actualizarPublicacion(HttpServletRequest request,
                                              @RequestParam(value = "id", required = false) Long id) throws PublicacionNoExistente {
        ModelMap model = new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {

            model.put("mensaje","accion invalida o no esta logeado o no es su publicacion");

            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        if(!(servicioPublicacion.busquedaPorId(id).getDuenioPublicacion().getId().equals(idUsuarioEnSesion))){

            model.put("mensaje","no se pudo encontrar la publicacion que quiere editar");

            return new ModelAndView("redirect:/home", model);
        }

        List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno((Usuario) request.getSession().getAttribute("usuarioEnSesion"));
        List<Mascota> mascotasDonantes = misMascotas.stream()
                .filter(Mascota::isDonante)
                .filter(Mascota::isAprobado)
                .collect(Collectors.toList());

        Publicacion publicacion =servicioPublicacion.busquedaPorId(id);

        model.put("publicacion", publicacion);
        model.put("misMascotas", mascotasDonantes);

        return new ModelAndView("actualizar-publicacion", model);
    }


    @RequestMapping(path = "/editar-publicacion")
    public ModelAndView editarPublicacion
            (HttpServletRequest request,
             @RequestParam(value = "id", required = false) Long id,
             @ModelAttribute("publicacion") Publicacion publicacionActualizada,
             @RequestParam("mascota") Long mascotaId) throws PublicacionNoExistente {

        ModelMap model= new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {
            model.put("mensaje","accion invalida o no esta logeado o no es su publicacion");
            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        if(servicioPublicacion.busquedaPorId(id).getDuenioPublicacion().getId().equals(idUsuarioEnSesion)){

            Publicacion publicacion =servicioPublicacion.busquedaPorId(id);

            Mascota mascota = servicioMascota.buscarMascotaPorId(mascotaId);
            publicacionActualizada.setMascotaDonante(mascota);
            publicacionActualizada.setTipoDeSangre(mascota.getSangre());

            servicioPublicacion.editarPublicacion(publicacion.getId(), publicacionActualizada);

            model.put("mensaje","se edito correctamente su publicacion");
        }

        return new ModelAndView("redirect:/home", model);

    }

}
