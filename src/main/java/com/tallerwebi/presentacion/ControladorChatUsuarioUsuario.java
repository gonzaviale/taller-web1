package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@Controller
public class ControladorChatUsuarioUsuario {

    private ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario;

    @Autowired
    public ControladorChatUsuarioUsuario(ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario) {
        this.servicioMensajeUsuarioUsuario = servicioMensajeUsuarioUsuario;
    }

    @RequestMapping(path = "/getMessagesByUsers", method = RequestMethod.GET)
    public ModelAndView getMessagesByUsers(Long usuario2, Long usuario, HttpServletRequest request){
        try {
            ModelMap model = new ModelMap();
            Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
            Usuario usuario2Obj = servicioMensajeUsuarioUsuario.searchUser(usuario2);
            Usuario usuarioObj = servicioMensajeUsuarioUsuario.searchUser(usuario);
            model.put("usuarioEmisor", usuarioEnSesion);
            if(!Objects.equals(usuarioEnSesion.getId(), usuario2Obj.getId())){
                model.put("usuarioReceptor", usuario2Obj);
                ArrayList<MensajeUsuarioUsuario> messages = this.servicioMensajeUsuarioUsuario.getMessagesByUsers(usuarioEnSesion, usuario2Obj);
                model.put("listMessages", messages);
            } else{
                model.put("usuarioReceptor", usuarioObj);
                ArrayList<MensajeUsuarioUsuario> messages = this.servicioMensajeUsuarioUsuario.getMessagesByUsers(usuarioEnSesion, usuarioObj);
                model.put("listMessages", messages);
            }
            return new ModelAndView("chattUsersUsers", model);
        } catch (RuntimeException e) {
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("chattUsersUsers", model);
        }
    }

    @RequestMapping(path = "/procesarMensajeUsuarioUsuario", method = RequestMethod.POST)
    public ModelAndView enviarMensajeBancoUsuario
            (@ModelAttribute("mensajeUsuarioUsuario") String mensajeUsuarioUsuario,
             @ModelAttribute("usuarioReceptor") Long usuarioReceptor,
             HttpServletRequest request){

        ModelMap model = new ModelMap();
        model.put("publicaciones","");
        Usuario usuarioReceptorObj = servicioMensajeUsuarioUsuario.searchUser(usuarioReceptor);

        try{
            Usuario usuarioEmisor = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
            MensajeUsuarioUsuario mensajeEnviado = servicioMensajeUsuarioUsuario.enviarMensaje(mensajeUsuarioUsuario,
                    usuarioEmisor, usuarioReceptorObj);
            String mensajeBienvenida = "Bienvenido, " + usuarioEmisor.getNombre();
            model.addAttribute("mensajeBienvenida", mensajeBienvenida);
            model.addAttribute("rol", usuarioEmisor.getRol());
        }catch (Exception e){
            model.remove("mensajeUsuarioBanco");
            model.put("errorAlEnviarMensaje", e.getMessage());
        }
        return new ModelAndView("home", model);
    }

    @RequestMapping(path="/enviarMensajeUsuarioUsuario")
    public ModelAndView enviarMensajeABancoScoring(Long usuarioReceptor) {
        System.out.println(usuarioReceptor);
        ModelMap model = new ModelMap();
        model.put("usuarioReceptor", usuarioReceptor);
        return new ModelAndView("enviarMensajeUsuarioUsuario", model);
    }
}
