package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioUsuario;
import com.tallerwebi.presentacion.DTO.ChatMessageUsersDTO;
import org.json.JSONObject;
import com.tallerwebi.presentacion.DTO.ChatMessageDTO;
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

@Controller
public class ControladorWebSocket {
    private final ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario;
    private ServicioBot servicioBot;
    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco;

    @Autowired
    public ControladorWebSocket(ServicioBot servicioBot, ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco, ServicioMensajeUsuarioUsuario servicioMensajeUsuarioUsuario) {
        this.servicioBot = servicioBot;
        this.servicioMensajeUsuarioBanco = servicioMensajeUsuarioBanco;
        this.servicioMensajeUsuarioUsuario = servicioMensajeUsuarioUsuario;
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
    public String sendChatMessage(ChatMessageDTO chatMessage) throws Exception {
        Usuario user = servicioMensajeUsuarioBanco.searchUser(chatMessage.getUsuarioId());
        MensajeUsuarioBanco createMessage;
        JSONObject json = new JSONObject();
        if (chatMessage.getUserInSession() != null) {
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(), "Usuario", user, chatMessage.getBancoId());
        } else {
            createMessage = servicioMensajeUsuarioBanco.enviarMensaje(chatMessage.getMensaje(), "Banco", user, chatMessage.getBancoId());
        }
        json.put("mensaje", createMessage.getMensaje());
        json.put("usuario", createMessage.getUsuario().getId());
        json.put("banco", createMessage.getBanco().getId());
        json.put("emisor", createMessage.getEmisor());
        return json.toString();
    }

    @MessageMapping("/chatUserMessage")
    @SendTo("/topic/chatUser")
    public String sendChatUserMessage(ChatMessageUsersDTO chatMessage) throws Exception {
        MensajeUsuarioUsuario createMessage;
        JSONObject json = new JSONObject();
        System.out.println(chatMessage.getUsuarioEmisor());
        System.out.println(chatMessage.getUsuarioEmisor());
        System.out.println(chatMessage.getMensaje());

        Usuario emisor = servicioMensajeUsuarioUsuario.searchUser(chatMessage.getUsuarioEmisor());
        Usuario receptor = servicioMensajeUsuarioUsuario.searchUser(chatMessage.getUsuarioEmisor());
        createMessage = servicioMensajeUsuarioUsuario.enviarMensaje(chatMessage.getMensaje(), emisor, receptor);
        json.put("mensaje", createMessage.getMensaje());
        json.put("usuarioEmisor", createMessage.getUsuarioEmisor());
        json.put("usuarioReceptor", createMessage.getUsuarioReceptor());
        return json.toString();
    }



    @RequestMapping(path = "/chat")
    public ModelAndView chat() {
        return new ModelAndView("chatbot");
    }


}
