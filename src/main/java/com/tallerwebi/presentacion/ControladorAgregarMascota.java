package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
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
import java.util.List;

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

        if (servicioMascota.verificarEstado(mascotaAux.getTipo(), mascotaAux.getPeso(), mascotaAux.getAnios()) != null){
            modelo.put("mensaje", servicioMascota.verificarEstado(mascotaAux.getTipo(), mascotaAux.getPeso(), mascotaAux.getAnios()));
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

    @RequestMapping(path = "/editar-mascota")
    public ModelAndView actualizarMascota(HttpServletRequest request,
                                              @RequestParam(value = "id", required = false) Long id) {
        ModelMap model = new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {

            model.put("mensaje","accion invalida o no esta logeado");

            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        Mascota mascota= servicioMascota.buscarMascotaPorId(id);

        if(!(mascota.getDuenio().getId().equals(idUsuarioEnSesion))){

            model.put("mensaje","no se pudo encontrar la mascota que quiere editar");

            return new ModelAndView("redirect:/home", model);
        }

        List <String> tipoDeSangre= servicioMascota.obtenerSangreSegunTipoDeMascota(mascota.getTipo());

        model.put("mascota", mascota);
        model.put("sangres",tipoDeSangre);

        return new ModelAndView("editar-mascota", model);
    }

    @RequestMapping(path = "/actualizar-mascota")
    public ModelAndView editarPublicacion
            (HttpServletRequest request,
             @ModelAttribute("mascota") Mascota mascota,
             @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes) {

        ModelMap model= new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {
            model.put("mensaje","accion invalida o no esta logeado o no es su publicacion");
            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        Mascota mascotaBuscada=servicioMascota.buscarMascotaPorId(mascota.getId());

        mascota.setDonante(mascotaBuscada.isDonante());
        mascota.setReceptor(mascotaBuscada.isReceptor());

        if (!servicioMascota.isEdadApropiadaDonante(mascota) && mascota.isDonante()) {
            model.put("mensaje","La mascota debe tener entre 1 y 8 anios.");
            return new ModelAndView("redirect:/home", model);
        }

        if (!servicioMascota.isPesoCorrectoCanino(mascota) && mascota.isDonante() && mascota.getTipo().equalsIgnoreCase("Canino")) {
            model.put("mensaje", "El peso del canino donante debe ser mayor a 25 kg.");
            return new ModelAndView("redirect:/home", model);
        }

        if (!servicioMascota.isPesoCorrectoFelino(mascota) && mascota.isDonante() && mascota.getTipo().equalsIgnoreCase("Felino")) {
            model.put("mensaje", "El peso del felino donante debe ser mayor a 3.5 kg.");
            return new ModelAndView("redirect:/home", model);
        }


        if(mascotaBuscada.getDuenio().getId().equals(idUsuarioEnSesion)){

            this.guardarImagenes(imagenes,mascotaBuscada.getId());

            servicioMascota.editarMascota(mascota);

            model.put("mensaje","se edito correctamente su publicacion");

        }else{

            model.put("mensaje","no es dueño de la mascota o no se pudo editar");
        }

        return new ModelAndView("redirect:/home", model);
    }

    @RequestMapping(path = "/eliminar-mascota")
    public ModelAndView eliminarMascota(HttpServletRequest request, @RequestParam(value = "id") Long id) {

        ModelMap model= new ModelMap();

        if (request == null || request.getSession().getAttribute("usuarioEnSesion") == null) {
            model.put("mensaje","accion invalida o no esta logeado");
            return new ModelAndView("redirect:/home",model);
        }

        Long idUsuarioEnSesion=  (Long) request.getSession().getAttribute("usuarioId");

        Mascota mascotaBuscada=servicioMascota.buscarMascotaPorId(id);

        if(mascotaBuscada!=null && mascotaBuscada.getDuenio().getId().equals(idUsuarioEnSesion) && servicioMascota.eliminarMascota(mascotaBuscada)){

            model.put("mensaje","se elimino correctamente su mascota");
        }else{

            model.put("mensaje","accion invalida no se puede eliminar pertenece a una solicitud o publicacion");
        }

        return new ModelAndView("redirect:/home", model);
    }

}