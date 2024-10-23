package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;

import java.util.ArrayList;
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

    List<BancoConTiposDeSangre> obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(String sangreBuscada, String tipoProducto);

    PaqueteDeSangre buscarSangreXId(int paqueteId);

    ArrayList<Banco> searchBankByScore();

    ArrayList<Banco> searchBankByScoreAndBlood(String sangre);

    void actualizarBanco(Banco banco);
}
