package com.tallerwebi.dominio;

public interface RepositorioBanco {


    public default Object buscarPorId(int l) {


        return null;
    }



    void guardar(Banco banco);
}
