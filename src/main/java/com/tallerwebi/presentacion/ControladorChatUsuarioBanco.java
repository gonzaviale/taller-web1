package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
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

    public ControladorChatUsuarioBanco(ServicioMensajeUsuarioBanco servicioMensajeUsuario) {
        this.servicioMensajeUsuario = servicioMensajeUsuario;
    }

    @RequestMapping(path = "/getAllMessages", method = RequestMethod.GET)
    public ModelAndView getAllMessages(HttpServletRequest request) {
        try{
            ModelMap model = new ModelMap();
            Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
            model.put("listMessages", this.servicioMensajeUsuario.getMessages(usuarioEnSesion));
            return new ModelAndView("messageUser", model);
        } catch(RuntimeException e){
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("messageUser", model);
        }
    }

    @RequestMapping(path = "/getMessagesByIds", method = RequestMethod.GET)
    public ModelAndView getMessagesById(Long usuarioId, Long bancoId){
        try {
            ModelMap model = new ModelMap();
            ArrayList<MensajeUsuarioBanco> messages = this.servicioMensajeUsuario.getMessagesByIds(usuarioId, bancoId);
            model.put("listMessages", messages);
            model.put("usuario", usuarioId);
            model.put("banco", bancoId);
            return new ModelAndView("chattUsers", model);
        } catch (RuntimeException e) {
            ModelMap model = new ModelMap();
            model.put("errorMessage", e.getMessage());
            return new ModelAndView("chattUsers", model);
        }
    }
}
