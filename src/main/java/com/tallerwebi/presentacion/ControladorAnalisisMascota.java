package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.ServicioImagenes;
import com.tallerwebi.dominio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorAnalisisMascota {
    private ModelMap modelo = new ModelMap();
    private ServicioImagenes servicioImagenes;
    private ServicioMascota servicioMascota;

    @Autowired
    public ControladorAnalisisMascota(ServicioImagenes servicioImagenes, ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
        this.servicioImagenes = servicioImagenes;
    }

    @RequestMapping(path = "/ver-solicitudes-donantes")
    public ModelAndView verSolicitudesDonantes() {
        List<Mascota> mascotasEnRevision = servicioMascota.obtenerMascotasEnRevision();
        Map<Mascota, List<String>> mascotasConImagenes = obtenerMascotasConImagenes(mascotasEnRevision);
        modelo.put("mascotasConImagenes", mascotasConImagenes);
        return new ModelAndView("ver-solicitudes-donantes", modelo);
    }

    @RequestMapping(path = "/aprobar", method = RequestMethod.POST)
    public ModelAndView aprobar(@RequestParam(name="mascotaId") Long id) {
        servicioMascota.aprobarMascotaDonante(id);
        return new ModelAndView("/ver-solicitudes-donantes");
    }

    @RequestMapping(path = "/rechazar", method = RequestMethod.POST)
    public ModelAndView rechazar(@RequestParam(name="mascotaId") Long id) {
        servicioMascota.rechazarMascotaDonante(id);
        return new ModelAndView("/ver-solicitudes-donantes");
    }

    public Map<Mascota, List<String>> obtenerMascotasConImagenes(List<Mascota> mascotas) {
        Map<Mascota, List<String>> mascotaConImagenes = new HashMap<>();
        for (Mascota mascota : mascotas) {

            List<String> imagenesMascota = servicioImagenes.obtenerImagenesPorUsuario(mascota.getId());

            mascotaConImagenes.put(mascota, imagenesMascota);
        }

        return mascotaConImagenes;
    }
}