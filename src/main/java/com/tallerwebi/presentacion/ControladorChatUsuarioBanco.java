package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class ControladorChatUsuarioBanco {
    private ServicioMensajeUsuarioBanco servicioMensajeUsuario;
    private ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario;

    public ControladorChatUsuarioBanco(ServicioMensajeUsuarioBanco servicioMensajeUsuario,
                                       ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario) {
        this.servicioMensajeUsuario = servicioMensajeUsuario;
        this.servicioMensajeUsuarioUsuario = servicioMensajeUsuarioUsuario;
    }

    @RequestMapping(path = "/getAllMessages", method = RequestMethod.GET)
    public ModelAndView getAllMessages(HttpServletRequest request) {
        try{
            ModelMap model = new ModelMap();
            Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
            model.put("listMessagesUsers", this.servicioMensajeUsuarioUsuario.getMessages(usuarioEnSesion));
            model.put("listMessages", this.servicioMensajeUsuario.getMessages(usuarioEnSesion));
            return new ModelAndView("messageUser", model);
        } catch(RuntimeException e){
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("messageUser", model);
        }
    }

    @RequestMapping(path = "/getAllMessagesBank", method = RequestMethod.GET)
    public ModelAndView getAllMessagesBank(HttpServletRequest request) {
        try{
            ModelMap model = new ModelMap();
            Long bankId = (Long) request.getSession().getAttribute("idBanco");
            model.put("idBanco", bankId);
            model.put("listMessages", this.servicioMensajeUsuario.getMessagesBank(bankId));
            return new ModelAndView("messageUser", model);
        } catch(RuntimeException e){
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("messageUser", model);
        }
    }

    @RequestMapping(path = "/getMessagesByIds", method = RequestMethod.GET)
    public ModelAndView getMessagesById(Long usuarioId, Long bancoId, HttpServletRequest request){
        try {
            ModelMap model = new ModelMap();
            ArrayList<MensajeUsuarioBanco> messages = this.servicioMensajeUsuario.getMessagesByIds(usuarioId, bancoId);
            model.put("listMessages", messages);
            model.put("usuario", usuarioId);
            model.put("banco", bancoId);
            if(request.getSession().getAttribute("usuarioEnSesion")!=null)
                model.put("sesion", request.getSession().getAttribute("usuarioEnSesion"));
            if(request.getSession().getAttribute("idBanco")!=null)
                model.put("idBanco", request.getSession().getAttribute("idBanco"));
            return new ModelAndView("chattUsers", model);
        } catch (RuntimeException e) {
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("chattUsers", model);
        }
    }
}
