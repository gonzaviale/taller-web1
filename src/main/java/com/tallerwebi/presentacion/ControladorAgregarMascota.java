package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Canino;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
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
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
    }

    @RequestMapping(path = "/agregar-mascota-donante")
    public ModelAndView formularioDonante() {
        return new ModelAndView("agregar-mascota-donante");
    }

    @PostMapping("/agregar-donante")
    public ModelAndView agregarDonante(@RequestParam("nombre") String nombre,
                                       @RequestParam("anios") int anios,
                                       @RequestParam("peso") float peso,
                                       @RequestParam("tipo") String tipo,
                                       @RequestParam("transfusion") String transfusion,
                                       HttpServletRequest request) {
        if (transfusion.equals("Si")){
            modelo.put("errorTransfusion", "Un animal ya transfundido no puede ser donante");
            return new ModelAndView("agregar-mascota-donante", modelo);
        }
        Mascota mascota;
        Usuario duenoMascota = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (tipo.equals("Felino")) {
            mascota = new Felino();
            mascota.setNombre(nombre);
            mascota.setAnios(anios);
            mascota.setPeso(peso);
            mascota.setTipo(tipo);
            mascota.setDonante(true);
            mascota.setRevision(true);
            mascota.setAprobado(false);
            mascota.setRechazado(false);
            mascota.setDuenio(duenoMascota);

            servicioMascota.registrarMascota(mascota);
        }
        if (tipo.equals("Canino")) {
            mascota = new Canino();
            mascota.setNombre(nombre);
            mascota.setAnios(anios);
            mascota.setPeso(peso);
            mascota.setTipo(tipo);
            mascota.setDonante(true);
            mascota.setRevision(true);
            mascota.setAprobado(false);
            mascota.setRechazado(false);
            mascota.setDuenio(duenoMascota);

            servicioMascota.registrarMascota(mascota);
        }

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