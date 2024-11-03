package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import com.tallerwebi.dominio.servicio.ServicioSolicitudAUnaPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class ControladorPerfil {

    private final ServicioPerfil servicioPerfil;
    private final ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion;
    private final ServicioImagenes servicioImagenes;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil, ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacion, ServicioImagenes servicioImagenes) {
        this.servicioPerfil = servicioPerfil;
        this.servicioSolicitudAUnaPublicacion = servicioSolicitudAUnaPublicacion;
        this.servicioImagenes= servicioImagenes;
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

            anidirFotoDePerfil(usuarioBuscado, model);

            aniadirListado(usuarioBuscado, model,listar);

            return new ModelAndView("perfil", model);
        }

        return new ModelAndView("redirect:/home");
    }

    private void anidirFotoDePerfil(Usuario usuarioBuscado, ModelMap model) {
        //cargar imagen al perfil si la tiene
        List <String> nombreDeArchivoImagen= servicioImagenes.obtenerImagenesPorUsuario(usuarioBuscado.getId());
        if(!(nombreDeArchivoImagen.isEmpty())){

            for(String archivo: nombreDeArchivoImagen){

                if(archivo.startsWith(usuarioBuscado.getId().toString() + "_perfil")){

                    model.addAttribute("foto",archivo);
                    return;
                }
            }
        }
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

    @RequestMapping("subirFoto")
    public ModelAndView subirFoto(HttpServletRequest request,
                                  @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes){

        ModelMap modelo= new ModelMap();

        Usuario usuarioBuscado=null;
        Usuario usuarioEnSesion= (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        if(usuarioEnSesion!=null){
            usuarioBuscado = servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId());
        }

        if(usuarioBuscado==null){
            modelo.put("mensaje","error al encontrar al usuario logeado");
            return new ModelAndView("redirect:/home", modelo);
        }

        if (imagenes == null || imagenes.length == 0) {
            modelo.put("mensaje", "imagen no valida");
            return new ModelAndView("redirect:/miPerfil", modelo);
        }

        try {
            servicioImagenes.guardarFotoDePerfilUsuario(imagenes, usuarioBuscado.getId());
            modelo.put("mensaje","se actualizo correctamente la imagen de su perfil");
        } catch (IOException e) {
            modelo.put("mensaje","ocurrio un error");
        }

        return new ModelAndView("redirect:/miPerfil", modelo);
    }

    @RequestMapping("eliminarFoto")
    public ModelAndView eliminarFoto(HttpServletRequest request) {

        ModelMap modelo= new ModelMap();

        Usuario usuarioBuscado=null;
        Usuario usuarioEnSesion= (Usuario) request.getSession().getAttribute("usuarioEnSesion");
        if(usuarioEnSesion!=null){
            usuarioBuscado = servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId());
        }

        if(usuarioBuscado==null){
            modelo.put("mensaje","error al encontrar al usuario logeado");
            return new ModelAndView("redirect:/home", modelo);
        }
        try {
        servicioImagenes.eliminarFotoDePerfil(usuarioBuscado.getId());
        modelo.put("mensaje","se elimino su imagen de perfil");
        } catch (IOException e) {
            modelo.put("mensaje","ocurrio un error");
        }

        return new ModelAndView("redirect:/miPerfil", modelo);
    }

}
