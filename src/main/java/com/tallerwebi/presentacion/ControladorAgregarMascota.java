package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Canino;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.infraestructura.RepositorioMascotaImpl;
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
    private final RepositorioMascotaImpl repositorioMascota;
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota, ServicioImagenes servicioImagenes, RepositorioMascotaImpl repositorioMascota) {
        this.servicioMascota = servicioMascota;
        this.servicioImagenes = servicioImagenes;
        this.repositorioMascota = repositorioMascota;
    }

    @RequestMapping(path = "/agregar-mascota")
    public ModelAndView agregarMascota() {
        return new ModelAndView("agregar-mascota");
    }

    @RequestMapping(path = "/agregar-mascota-donante")
    public ModelAndView formularioDonante(HttpServletRequest request) {
        modelo.put("mascota", new Mascota());
        return new ModelAndView("agregar-mascota-donante", modelo);
    }

    @RequestMapping(path = "/agregar-mascota-receptora")
    public ModelAndView formularioReceptora(HttpServletRequest request) {
        return new ModelAndView("agregar-mascota-receptora");
    }

    @RequestMapping(path = "definir-tipo-donante")
    public ModelAndView definirTipoDonante(@ModelAttribute("mascota") Mascota mascotaAux,
                                           @RequestParam(value = "transfusion", required = false) String transfusion,
                                           HttpServletRequest request) {
        modelo.clear();
        if (transfusion == null || transfusion.isEmpty()) {
            modelo.put("errorTransfusion", "Es obligatorio ingresar si el animal recibió o no una transfusión");
            return new ModelAndView("agregar-mascota", modelo);
        }
        if (transfusion.equals("Si")) {
            modelo.put("mensaje", "No puede registrar a su mascota si ya recibio una transfusión");
            return new ModelAndView("redirect:/home", modelo);
        }

        if (mascotaAux.getNombre() == null || mascotaAux.getNombre().isEmpty()) {
            modelo.put("errorNombre", "El nombre de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (mascotaAux.getAnios() == null || mascotaAux.getAnios() == 0) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (mascotaAux.getPeso() == null || mascotaAux.getPeso() == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (mascotaAux.getTipo() == null || mascotaAux.getTipo().isEmpty()) {
            modelo.put("errorTipo", "Es obligatorio ingresar la especie de su mascota");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (!mascotaAux.getTipo().equals("Canino") && !mascotaAux.getTipo().equals("Felino")) {
            modelo.put("errorTipo", "El tipo de mascota no es válido");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (mascotaAux.getTipo().equals("Canino") && (mascotaAux.getPeso() < 25f || mascotaAux.getAnios() <= 1 || mascotaAux.getAnios() >= 8)) {
            modelo.put("mensaje", "Para que un perro sea donante debe pesar más de 25 kilos y tener entre 1 y 8 años");
            return new ModelAndView("home", modelo);
        } else if (mascotaAux.getTipo().equals("Felino") && (mascotaAux.getPeso() < 3.5f || mascotaAux.getAnios() <= 1 || mascotaAux.getAnios() >= 8)) {
            modelo.put("mensaje", "Para que un gato sea donante debe pesar más de 3,5 kilos y tener entre 1 y 8 años");
            return new ModelAndView("redirect:/home", modelo);
        }

        modelo.put("mascotaAux", mascotaAux);

        if (mascotaAux.getTipo().equals("Canino")) {
            return new ModelAndView("agregar-donante-canino", modelo);
        } else {
            return new ModelAndView("agregar-donante-felino", modelo);
        }
    }

    @PostMapping("/agregar-donante")
    public ModelAndView agregarDonante(@ModelAttribute("mascota") Mascota mascota,
                                       @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes,
                                       HttpServletRequest request) {

        modelo.clear();
        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        mascota.setDuenio(duenoMascota);
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

        if (anios == null || anios == 0) {
            modelo.put("errorEdad", "La edad de la mascota es obligatoria");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (peso == null || peso == 0f) {
            modelo.put("errorPeso", "El peso de la mascota es obligatorio");
            return new ModelAndView("agregar-mascota", modelo);
        }

        if (tipo == null || tipo.isEmpty()) {
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