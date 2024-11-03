package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;

import java.util.ArrayList;
import java.util.Collection;

public interface RepositorioMensajeUsuarioBanco {
    public MensajeUsuarioBanco crearMensaje(String mensaje, String emisor, Usuario usuario, Banco banco);
    public Banco searchBankById(Long banco);
    public ArrayList<MensajeUsuarioBanco> getMessagesByUser(Usuario usuario);
    public ArrayList<MensajeUsuarioBanco> getMessagesByUserAndBank(Long usuario, Long banco);
    public Usuario searchUser(Long usuario);
    public ArrayList<MensajeUsuarioBanco> getMessagesByBank(Long banco);
}
