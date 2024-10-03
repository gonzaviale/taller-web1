package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.BancoConTiposDeSangre;

import java.util.List;

public interface RepositorioBanco {
    Banco guardar(Banco banco);
    PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete, Banco banco);
    Banco buscarBanco(String email, String password);
    Banco buscarPorId(Long idBanco);
    PaqueteDeSangre buscarSangre(String s);
    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);
    List<BancoConTiposDeSangre> obtenerLaCoincidenciaEnSangreDeTodosLosBancos(String sangreBuscada);
    List<BancoConTiposDeSangre> obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos(String tipoProducto);
    List<BancoConTiposDeSangre> obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(String tipoProducto);
}
