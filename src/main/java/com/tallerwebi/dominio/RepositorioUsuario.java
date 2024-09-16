package com.tallerwebi.dominio;


public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Banco guardarBanco(Banco banco);
    Banco buscarBanco(String email, String password);


}

