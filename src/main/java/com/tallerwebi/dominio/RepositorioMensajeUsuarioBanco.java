package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;

import java.util.ArrayList;
import java.util.Collection;

public interface RepositorioMensajeUsuarioBanco {
     MensajeUsuarioBanco crearMensaje(String mensaje, String emisor, Usuario usuario, Banco banco);
     Banco searchBankById(Long banco);
     ArrayList<MensajeUsuarioBanco> getMessagesByUser(Usuario usuario);
     ArrayList<MensajeUsuarioBanco> getMessagesByUserAndBank(Long usuario, Long banco);
     Usuario searchUser(Long usuario);
     ArrayList<MensajeUsuarioBanco> getMessagesByBank(Long banco);
     void crearArchivo(String mensaje, String emisor, Usuario usuario, Long banco, String pdf);
}
