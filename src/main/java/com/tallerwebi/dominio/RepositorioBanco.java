package com.tallerwebi.dominio;

public interface RepositorioBanco {




    Banco guardar(Banco banco);

    Banco buscarBanco(String email, String password);

    Banco buscarPorId(Long idBanco);
}
