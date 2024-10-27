package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import com.tallerwebi.dominio.servicio.ServicioSolicitudAUnaPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorPerfil {

    private final ServicioPerfil servicioPerfil;
    private final ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil, ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion) {
        this.servicioPerfil = servicioPerfil;
        this.servicioSolicitudAUnaPublicacion = servicioSolicitudAUnaPublicacion;
    }

    @RequestMapping("/miPerfil")
    public ModelAndView irAMiPerfil(
            @RequestParam(name = "mensaje", required = false) String mensaje,
            HttpServletRequest request,
            @RequestParam(value = "listar", required = false) String listar) {

        ModelMap model = new ModelMap();
        Usuario usuarioBuscado=null;
        Usuario usuarioEnSesion= (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        if(usuarioEnSesion!=null){
            usuarioBuscado = servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId());
        }

        if (usuarioBuscado != null) {
            model.addAttribute("miperfil",Boolean.TRUE);
            model.addAttribute("usuarioBuscado", usuarioBuscado);
            model.addAttribute("mensaje",mensaje);

            List<SolicitudAUnaPublicacion> solicitudesRecibidas = servicioSolicitudAUnaPublicacion.traerSolicitudesPendientesDelUsuario(usuarioBuscado);
            model.addAttribute("solicitudesRecibidas", solicitudesRecibidas);

            List<SolicitudAUnaPublicacion> solicitudesAceptadas = servicioSolicitudAUnaPublicacion.traerSolicitudesAceptadasDelUsuario(usuarioBuscado);
            model.addAttribute("solicitudesAceptadas", solicitudesAceptadas);

            List<SolicitudAUnaPublicacion> solicitudesRechazadas = servicioSolicitudAUnaPublicacion.traerSolicitudesRechazadasDelUsuario(usuarioBuscado);
            model.addAttribute("solicitudesRechazadas", solicitudesRechazadas);

            aniadirListado(usuarioBuscado, model,listar);

            return new ModelAndView("perfil", model);
        }

        return new ModelAndView("redirect:/home");
    }

    private void aniadirListado(Usuario usuarioBuscado, ModelMap model,String listar) {

        if(listar!=null && listar.equals("mascotas")){
            List<Mascota> misMascotas=servicioPerfil.obtenerMascotasDelUsuario(usuarioBuscado.getId());
            model.addAttribute("listaMascotas",misMascotas);
        }else if(listar!=null && listar.equals("publicaciones")){
            List<Publicacion> publicaciones=servicioPerfil.obtenerPublicacionesDelUsuario(usuarioBuscado.getId());
            model.addAttribute("publicaciones",publicaciones);
        }else{
            List<Mascota> misMascotas=servicioPerfil.obtenerMascotasDelUsuario(usuarioBuscado.getId());
            model.addAttribute("listaMascotas",misMascotas);
        }

    }

    @RequestMapping("/{nombre}.{id}")
    public ModelAndView mostrarPerfil(@PathVariable Long id,
                                      HttpServletRequest request,
                                      @RequestParam(value = "listar", required = false) String listar) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if(usuario!=null && id.equals(usuario.getId())){
            return new ModelAndView("redirect:/miPerfil");
        }

        Usuario usuarioBuscado = servicioPerfil.buscarUsuarioPorId(id);

        ModelMap model= new ModelMap();
        if (usuarioBuscado != null) {
            model.addAttribute("miperfil",Boolean.FALSE);
            model.addAttribute("user", usuarioBuscado);
            this.aniadirListado(usuarioBuscado, model,listar);
            return new ModelAndView("perfil", model);
        }
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping("/editarperfil")
    public ModelAndView editarPerfil(
            HttpServletRequest request) {

        ModelMap model = new ModelMap();
        Usuario usuarioBuscado;
        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        if (usuarioEnSesion != null) {
            usuarioBuscado = servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId());

            model.addAttribute("email",usuarioBuscado.getEmail());
            model.addAttribute("nombre",usuarioBuscado.getNombre());
            model.addAttribute("apellido", usuarioBuscado.getApellido());
            model.addAttribute("contrasena",usuarioBuscado.getPassword());

            return new ModelAndView("editarperfil",model);
        }

        return new ModelAndView("redirect:/home", new ModelMap());
    }

    @RequestMapping("/actualizarUsuario")
    public ModelAndView actualizarUsuario(HttpServletRequest request,
                                    @RequestParam String email,
                                    @RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam String password) {

        Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");

        if (usuarioEnSesion != null) {
            if (email != null && !email.trim().isEmpty()) {
                usuarioEnSesion.setEmail(email);
            }
            if (nombre != null && !nombre.trim().isEmpty()) {
                usuarioEnSesion.setNombre(nombre);
            }
            if (apellido != null && !apellido.trim().isEmpty()) {
                usuarioEnSesion.setApellido(apellido);
            }
            if (password != null && !password.trim().isEmpty()) {
                usuarioEnSesion.setPassword(password);
            }
            servicioPerfil.actualizarUsuario(usuarioEnSesion);
            ModelMap model = new ModelMap("mensaje", "usuario actualizado");
            return new ModelAndView("redirect:/miPerfil", model);
        }

        return new ModelAndView("redirect:/login");
    }

}
