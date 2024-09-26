package com.tallerwebi.presentacion;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorWebSocket {

    // Método que maneja los mensajes entrantes y los envía a todos los suscriptores
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        // Devuelve el mensaje enviado al topic para que todos los clientes lo reciban
        return message;
    }

    @RequestMapping(path = "/chat")
    public ModelAndView chat() {
        return new ModelAndView("chat");
    }


}
