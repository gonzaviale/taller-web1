package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Bot;
import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioBot;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorWebSocket {
    private ServicioBot servicioBot;
    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco;

    @Autowired
    public ControladorWebSocket(ServicioBot servicioBot, ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco) {
        this.servicioBot = servicioBot;
        this.servicioMensajeUsuarioBanco = servicioMensajeUsuarioBanco;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        Bot respuestaBot = servicioBot.solicitarRespuesta(message);
        if(respuestaBot==null){
            return "<p style='color:red'>Bot: Ops, no entiendo tu mensaje, intentelo de nuevo</p>";
        }
        return "Tu: "+message + "<br><br>" + "Bot: " + respuestaBot.getRespuesta();
    }

    @MessageMapping("/chatMessage")
    @SendTo("/topic/chat")
    public MensajeUsuarioBanco sendChatMessage(ChatMessage chatMessage, HttpServletRequest request) throws Exception {
        Usuario user = servicioMensajeUsuarioBanco.searchUser(chatMessage.getUsuarioId());
        System.out.println(chatMessage.getUsuarioId());
        System.out.println("Usuario: " + chatMessage.getUsuarioId() + "Holaaaaaaaaaa");
        System.out.println(chatMessage.getMensaje());
        System.out.println(chatMessage.getBancoId());
        MensajeUsuarioBanco createMessage;
        if(request.getSession().getAttribute("usuarioEnSesion")!= null){
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(),"Usuario", user, chatMessage.getBancoId());
        } else {
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(),"Banco", user, chatMessage.getBancoId());
        }
        System.out.println(createMessage);
        return createMessage;
    }

    @RequestMapping(path = "/chat")
    public ModelAndView chat() {
        return new ModelAndView("chatbot");
    }

    public static class ChatMessage {
        private Long usuarioId;
        private Long bancoId;
        private String mensaje;

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public Long getBancoId() {
            return bancoId;
        }

        public void setBancoId(Long bancoId) {
            this.bancoId = bancoId;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
