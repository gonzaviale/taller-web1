package com.tallerwebi.presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public String sendChatMessage(ChatMessage chatMessage) throws Exception {
        Usuario user = servicioMensajeUsuarioBanco.searchUser(chatMessage.getUsuarioId());
        MensajeUsuarioBanco createMessage;
        if(chatMessage.getUserInSession()!=null){
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(),"Usuario", user, chatMessage.getBancoId());
            System.out.println("Mensaje enviado: "+createMessage.toString());
            System.out.println("Mensaje enviado: holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(createMessage);
            System.out.println(jsonString);
            return createMessage.toString();
        }
        else{
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(),"Banco", user, chatMessage.getBancoId());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(createMessage);
            return jsonString;
        }
    }

    @RequestMapping(path = "/chat")
    public ModelAndView chat() {
        return new ModelAndView("chatbot");
    }

    public static class ChatMessage {
        private Long usuarioId;
        private Long bancoId;
        private String mensaje;
        private Long bankInSession;

        public Usuario getUserInSession() {
            return userInSession;
        }

        public void setUserInSession(Usuario userInSession) {
            this.userInSession = userInSession;
        }

        public Long getBankInSession() {
            return bankInSession;
        }

        public void setBankInSession(Long bankInSession) {
            this.bankInSession = bankInSession;
        }

        private Usuario userInSession;

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
