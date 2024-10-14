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
    public ModelAndView agregarDonante(@RequestParam("nombre") String nombre,
                                       @RequestParam("anios") int anios,
                                       @RequestParam("peso") float peso,
                                       @RequestParam("tipo") String tipo,
                                       @RequestParam("transfusion") String transfusion,
                                       @RequestParam("imagenes") MultipartFile[] imagenes,
                                       HttpServletRequest request) {

        if (transfusion.equals("Si")) {
            modelo.put("errorTransfusion", "Un animal que ya recibió una transfusión no puede ser donante");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);

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
    public ModelAndView agregarReceptora(@RequestParam("nombre") String nombre,
                                         @RequestParam("anios") int anios,
                                         @RequestParam("peso") float peso,
                                         @RequestParam("tipo") String tipo,
                                         @RequestParam("imagenes") MultipartFile[] imagenes,
                                         HttpServletRequest request) {

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        Mascota mascota = crearMascotaSegunTipo(tipo);

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