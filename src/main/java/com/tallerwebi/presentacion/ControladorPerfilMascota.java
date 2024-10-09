package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorPerfilMascota {
    private final ServicioMascota servicioMascota;
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorPerfilMascota(ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
    }

    @RequestMapping(path = "/ver-mis-mascotas")
    public ModelAndView verMisMascotas(HttpServletRequest request){
        Usuario dueno = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        List<Mascota> misMascotas = servicioMascota.obtenerMascotasPorDueno(dueno);
        if (misMascotas == null) {
            modelo.put("usuario", "el usuaario es null");
            return new ModelAndView("ver-mis-mascotas", modelo);
        }
       modelo.put("misMascotas", misMascotas);
        return new ModelAndView("ver-mis-mascotas", modelo);
    }
}
