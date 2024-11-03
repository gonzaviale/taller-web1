package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Canino;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ControladorAgregarMascota {

    private final ServicioMascota servicioMascota;
    private final ServicioImagenes servicioImagenes;
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota, ServicioImagenes servicioImagenes) {
        this.servicioMascota = servicioMascota;
        this.servicioImagenes = servicioImagenes;
    }

    @RequestMapping(path = "/agregar-mascota")
    public ModelAndView agregarMascota() {
        return new ModelAndView("agregar-mascota");
    }

    @RequestMapping(path = "/agregar-mascota-donante")
    public ModelAndView formularioDonante(HttpServletRequest request) {

        String requestedWith = request.getHeader("X-Requested-With");

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (!"XMLHttpRequest".equals(requestedWith) || usuarioEnSesion==null || !(usuarioEnSesion.getRol().equals("dueño mascota"))) {

            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("agregar-mascota-donante :: formulario");
    }

    @RequestMapping(path = "/agregar-mascota-receptora")
    public ModelAndView formularioReceptora(HttpServletRequest request) {

        String requestedWith = request.getHeader("X-Requested-With");

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (!"XMLHttpRequest".equals(requestedWith) || usuarioEnSesion==null || !(usuarioEnSesion.getRol().equals("dueño mascota"))) {

            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("agregar-mascota-receptora :: formulario");
    }

    @PostMapping("/agregar-donante")
    public ModelAndView agregarDonante(@RequestParam(value = "nombre", required = false) String nombre,
                                       @RequestParam(value = "anios", required = false) Integer anios,
                                       @RequestParam(value = "peso", required = false) Float peso,
                                       @RequestParam(value = "tipo", required = false) String tipo,
                                       @RequestParam(value = "transfusion", required = false) String transfusion,
                                       @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes,
                                       HttpServletRequest request) {

        modelo.clear();
      if (transfusion == null || transfusion.isEmpty()){
            modelo.put("errorTransfusion", "Es obligatorio ingresar si el animal recibió o no una transfusión");
            return new ModelAndView("agregar-mascota", modelo);
      }
        if (transfusion.equals("Si")) {
            modelo.put("mensaje", "no puede registrar a su mascota si ya recibio una transfusion");
            return new ModelAndView("redirect:/home", modelo);
        }

        if (imagenes == null || imagenes.length == 0) {
            modelo.put("errorImagenes", "Una mascota no se puede registrar sin imágenes de sus estudios");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (nombre == null || nombre.isEmpty()) {
            modelo.put("errorNombre", "El nombre de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (anios == null|| anios == 0 ) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (peso == null || peso == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if(tipo == null || tipo.isEmpty()){
            modelo.put("errorTipo", "Es obligatorio ingresar la especie de su mascota");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (tipo.equals("Canino") && (peso < 25f || anios <= 1 || anios >= 8)) {
            modelo.put("mensaje", "Para que un perro sea donante debe pesar mas de 25 kilos y tener entre 1 y 8 anios");
            return new ModelAndView("redirect:/home", modelo);
        } else if (tipo.equals("Felino") && (peso < 3.5f || anios <= 1 || anios >= 8)) {
            modelo.put("mensaje", "Para que un gato sea donante debe pesar mas de 3,5 kilos y tener entre 1 y 8 anios");
            return new ModelAndView("redirect:/home", modelo);
        }

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);
        if (mascota == null) {
            modelo.put("errorTipo", "El tipo de mascota no es válido");
            return new ModelAndView("agregar-mascota", modelo);
        }

        asignarAtributosComunes(mascota, nombre, anios, peso, tipo, duenoMascota);
        mascota.setDonante(true);

        servicioMascota.registrarMascota(mascota);

        try {
            servicioImagenes.guardarExamen(imagenes, mascota.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ModelMap model = new ModelMap("mensaje", "la mascota fue registrada correctamente");

        return new ModelAndView("redirect:/home", model);
    }

    @PostMapping("/agregar-receptora")
    public ModelAndView agregarReceptora(@RequestParam(value = "nombre", required = false) String nombre,
                                         @RequestParam(value = "anios", required = false) Integer anios,
                                         @RequestParam(value = "peso", required = false) Float peso,
                                         @RequestParam(value = "tipo", required = false) String tipo,
                                         @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes,
                                         HttpServletRequest request) {
        modelo.clear();

        if (imagenes == null || imagenes.length == 0) {
            modelo.put("errorImagenes", "Una mascota no se puede registrar sin imágenes de sus estudios");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (nombre == null || nombre.isEmpty()) {
            modelo.put("errorNombre", "El nombre de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (anios == null|| anios == 0 ) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (peso == null || peso == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if(tipo == null || tipo.isEmpty()){
            modelo.put("errorTipo", "Es obligatorio ingresar el tipo de mascota");
            return new ModelAndView("agregar-mascota", modelo);
        }

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);
        if (mascota == null) {
            modelo.put("errorTipo", "El tipo de mascota no es válido");
            return new ModelAndView("agregar-mascota", modelo);
        }

        asignarAtributosComunes(mascota, nombre, anios, peso, tipo, duenoMascota);
        mascota.setReceptor(true);

        servicioMascota.registrarMascota(mascota);

        try {
            servicioImagenes.guardarExamen(imagenes, mascota.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ModelMap model = new ModelMap("mensaje", "la mascota fue registrada correctamente");

        return new ModelAndView("redirect:/home", model);
    }

    private Mascota crearMascotaSegunTipo(String tipo) {
        switch (tipo) {
            case "Felino":
                return new Felino();
            case "Canino":
                return new Canino();
            default:
                return null;
        }
    }

    private void asignarAtributosComunes(Mascota mascota, String nombre, int anios, float peso, String tipo, Usuario duenoMascota) {
        mascota.setNombre(nombre);
        mascota.setAnios(anios);
        mascota.setPeso(peso);
        mascota.setRevision(true);
        mascota.setAprobado(false);
        mascota.setRechazado(false);
        mascota.setTipo(tipo);
        mascota.setDuenio(duenoMascota);
    }
}