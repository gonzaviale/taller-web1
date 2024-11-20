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
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota, ServicioImagenes servicioImagenes, RepositorioMascotaImpl repositorioMascota) {
        this.servicioMascota = servicioMascota;
        this.servicioImagenes = servicioImagenes;
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
        modelo.put("mascota", new Mascota());
        return new ModelAndView("agregar-mascota-receptora", modelo);
    }

    @RequestMapping(path = "/definir-tipo-receptor")
    public ModelAndView definirTipoReceptor(@ModelAttribute("mascota") Mascota mascotaAux) {
        modelo.clear();
        ModelAndView errorView = validarMascota(mascotaAux);
        if (errorView != null) {
            return errorView;
        }

        modelo.put("mascotaAux", mascotaAux);

        return new ModelAndView(
                mascotaAux.getTipo().equals("Canino") ? "agregar-receptor-canino" : "agregar-receptor-felino",
                modelo
        );
    }

    @RequestMapping(path = "/definir-tipo-donante")
    public ModelAndView definirTipoDonante(@ModelAttribute("mascota") Mascota mascotaAux,
                                           @RequestParam(value = "transfusion", required = false) String transfusion) {
        modelo.clear();

        if (transfusion == null || transfusion.isEmpty()) {
            modelo.put("errorTransfusion", "Es obligatorio ingresar si el animal recibió o no una transfusión");
            return new ModelAndView("agregar-mascota", modelo);
        }
        if ("Si".equals(transfusion)) {
            modelo.put("mensaje", "No puede registrar a su mascota si ya recibió una transfusión");
            return new ModelAndView("redirect:/home", modelo);
        }

        ModelAndView errorView = validarMascota(mascotaAux);
        if (errorView != null) {
            return errorView;
        }

        if (mascotaAux.getTipo().equals("Canino") && (mascotaAux.getPeso() < 25f || mascotaAux.getAnios() <= 1 || mascotaAux.getAnios() >= 8)) {
            modelo.put("mensaje", "Para que un perro sea donante debe pesar más de 25 kilos y tener entre 1 y 8 años");
            return new ModelAndView("home", modelo);
        } else if (mascotaAux.getTipo().equals("Felino") && (mascotaAux.getPeso() < 3.5f || mascotaAux.getAnios() <= 1 || mascotaAux.getAnios() >= 8)) {
            modelo.put("mensaje", "Para que un gato sea donante debe pesar más de 3,5 kilos y tener entre 1 y 8 años");
            return new ModelAndView("redirect:/home", modelo);
        }

        modelo.put("mascotaAux", mascotaAux);

        return new ModelAndView(
                mascotaAux.getTipo().equals("Canino") ? "agregar-donante-canino" : "agregar-donante-felino",
                modelo
        );
    }

    @PostMapping("/agregar-donante")
    public ModelAndView agregarDonante(@ModelAttribute("mascota") Mascota mascota,
                                       @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes,
                                       HttpServletRequest request) {
        modelo.clear();
        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        registrarMascota(mascota, duenoMascota, true, false);
        guardarImagenes(imagenes, mascota.getId());

        modelo.put("mensaje", "La mascota fue registrada correctamente");
        return new ModelAndView("redirect:/home", modelo);
    }

    @PostMapping("/agregar-receptora")
    public ModelAndView agregarReceptora(@ModelAttribute("mascota") Mascota mascota,
                                         @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes,
                                         HttpServletRequest request) {
        modelo.clear();
        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        registrarMascota(mascota, duenoMascota, false, true);
        guardarImagenes(imagenes, mascota.getId());

        modelo.put("mensaje", "La mascota fue registrada correctamente");
        return new ModelAndView("redirect:/home", modelo);
    }

    private ModelAndView validarMascota(Mascota mascotaAux) {
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

        return null;
    }

    private void registrarMascota(Mascota mascota, Usuario dueno, boolean esDonante, boolean esReceptor) {
        mascota.setDuenio(dueno);
        mascota.setRevision(true);
        mascota.setDonante(esDonante);
        mascota.setReceptor(esReceptor);
        servicioMascota.registrarMascota(mascota);
    }

    private void guardarImagenes(MultipartFile[] imagenes, Long mascotaId) {
        try {
            servicioImagenes.guardarExamen(imagenes, mascotaId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}