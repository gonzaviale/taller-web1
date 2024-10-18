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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    @RequestMapping(path = "/agregar-mascota-donante")
    public ModelAndView formularioDonante() {
        return new ModelAndView("agregar-mascota-donante");
    }

    @RequestMapping(path = "/agregar-mascota-receptora")
    public ModelAndView formularioReceptora() {
        return new ModelAndView("agregar-mascota-receptora");
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
            return new ModelAndView("agregar-mascota-donante", modelo);
        }
        if (transfusion.equals("Si")) {
            modelo.put("errorTransfusion", "Un animal que ya recibió una transfusión no puede ser donante");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (imagenes == null || imagenes.length == 0) {
            modelo.put("errorImagenes", "Una mascota no se puede registrar sin imágenes de sus estudios");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (nombre == null || nombre.isEmpty()) {
            modelo.put("errorNombre", "El nombre de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (anios == null|| anios == 0 ) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (peso == null || peso == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if(tipo == null || tipo.isEmpty()){
            modelo.put("errorTipo", "Es obligatorio ingresar el tipo de mascota");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (tipo.equals("Canino") && (peso < 25f || anios <= 1 || anios >= 8)) {
            modelo.put("errorPesoYEdad", "Para que un perro sea donante debe pesar más de 25 kilos y tener entre 1 y 8 años");
            return new ModelAndView("agregar-mascota-donante", modelo);
        } else if (tipo.equals("Felino") && (peso < 3.5f || anios <= 1 || anios >= 8)) {
            modelo.put("errorPesoYEdad", "Para que un gato sea donante debe pesar más de 3,5 kilos y tener entre 1 y 8 años");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);
        if (mascota == null) {
            modelo.put("errorTipo", "El tipo de mascota no es válido");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        asignarAtributosComunes(mascota, nombre, anios, peso, tipo, duenoMascota);
        mascota.setDonante(true);

        servicioMascota.registrarMascota(mascota);

        try {
            servicioImagenes.guardarExamen(imagenes, mascota.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("redirect:/home");
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
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (nombre == null || nombre.isEmpty()) {
            modelo.put("errorNombre", "El nombre de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (anios == null|| anios == 0 ) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if (peso == null || peso == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        if(tipo == null || tipo.isEmpty()){
            modelo.put("errorTipo", "Es obligatorio ingresar el tipo de mascota");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }
        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);
        if (mascota == null) {
            modelo.put("errorTipo", "El tipo de mascota no es válido");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        asignarAtributosComunes(mascota, nombre, anios, peso, tipo, duenoMascota);
        mascota.setReceptor(true);

        servicioMascota.registrarMascota(mascota);

        try {
            servicioImagenes.guardarExamen(imagenes, mascota.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("redirect:/home");
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