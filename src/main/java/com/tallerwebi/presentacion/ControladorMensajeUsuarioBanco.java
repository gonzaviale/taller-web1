package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorMensajeUsuarioBanco {

    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco;

    @Autowired
    public ControladorMensajeUsuarioBanco(ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco) {
        this.servicioMensajeUsuarioBanco = servicioMensajeUsuarioBanco;
    }

    @RequestMapping(path = "/enviarMensajeUsuarioBanco", method = RequestMethod.POST)
    public ModelAndView enviarMensajeUsuarioBanco
            (@ModelAttribute("mensajeUsuarioBanco") String mensajeUsuarioBanco,
             @ModelAttribute("idBanco") int idBanco,
             HttpServletRequest request){
        ModelMap model = new ModelMap();
        model.remove("mensajeUsuarioBanco");
        try{
            Long idBancoLong = Long.parseLong(String.valueOf(idBanco));
            Usuario usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
            MensajeUsuarioBanco mensajeEnviado = servicioMensajeUsuarioBanco.
                    enviarMensaje(mensajeUsuarioBanco, "Usuario", usuarioEnSesion, idBancoLong);
            model.put("mensajeUsuarioBanco", "Mensaje enviado al banco con exito");
            model.put("publicaciones", "");
        }catch (Exception e){
            model.remove("mensajeUsuarioBanco");
            model.put("errorAlEnviarMensaje", e.getMessage());
            model.put("publicaciones", "");
        }
        return new ModelAndView("home", model);
    }
}
