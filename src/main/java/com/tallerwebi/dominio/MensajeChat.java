package com.tallerwebi.dominio;

public class MensajeChat {
    private String message;

    // Constructor, getters y setters
    public MensajeChat() {}

    public MensajeChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
