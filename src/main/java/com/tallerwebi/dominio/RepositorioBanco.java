package com.tallerwebi.dominio;

public interface RepositorioBanco {




    Banco guardar(Banco banco);

    PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete);

    Banco buscarBanco(String email, String password);

    Banco buscarPorId(Long idBanco);

    Banco actualizar(Banco banco, PaqueteDeSangre paquete);
}
