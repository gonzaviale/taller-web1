package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ControladorAgregarMascota {
    private ModelMap modelo = new ModelMap();
    private ServicioMascota servicioMascota;

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
    }

    @RequestMapping(path = "/formulario-donante", method = RequestMethod.GET)
    public ModelAndView formularioDonante() {
        modelo.put("mascota", new Mascota());
        return new ModelAndView("agregar-mascota-donante", modelo);
    }

    @RequestMapping(path = "/agregar-donante", method = RequestMethod.POST)
    public ModelAndView agregarDonante(
            @ModelAttribute("mascota") Mascota mascota,
            @RequestParam (name = "imagenes")MultipartFile[] imagenes,
            HttpServletRequest request) {

        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        try {
            guardarImagenes(imagenes, mascota.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mascota.setDuenio(duenoMascota);
        mascota.setDonante(true);
        mascota.setRevision(true);
        mascota.setAprobado(false);
        mascota.setRechazado(false);

        servicioMascota.registrarMascota(mascota);

        return new ModelAndView("redirect:/home");
    }


    private void guardarImagenes(MultipartFile[] imagenes, Long id) throws IOException {
        String basePath = System.getProperty("user.dir");

        Path uploadDirectory = Paths.get(basePath, "src", "main", "webapp", "resources", "images", "subidas");

        if (Files.notExists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                String nombreOriginal = imagen.getOriginalFilename();

                if (nombreOriginal != null) {
                    String nuevoNombre = id + "_" + nombreOriginal;

                    Path path = uploadDirectory.resolve(nuevoNombre);

                    Files.write(path, imagen.getBytes());
                }
            }
        }
    }
}