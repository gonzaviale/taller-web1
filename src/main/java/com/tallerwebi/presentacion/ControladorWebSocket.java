package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Bot;
import com.tallerwebi.dominio.ServicioBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorWebSocket {
    private ServicioBot servicioBot;

    @Autowired
    public ControladorWebSocket(ServicioBot servicioBot) { this.servicioBot = servicioBot; }

    // Método que maneja los mensajes entrantes y los envía a todos los suscriptores
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        // Devuelve el mensaje enviado al topic para que todos los clientes lo reciban
        Bot respuestaBot = servicioBot.solicitarRespuesta(message);
        if(respuestaBot==null){
            return "<p style='color:red'>Bot: Ops, no entiendo tu mensaje, intentelo de nuevo</p>";
        }
        return "Tu: "+message + "<br><br>" + "Bot: " + respuestaBot.getRespuesta();
    }

    @RequestMapping(path = "/chat")
    public ModelAndView chat() {
        return new ModelAndView("chat");
    }


}
