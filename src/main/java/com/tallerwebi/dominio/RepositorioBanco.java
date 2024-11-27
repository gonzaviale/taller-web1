package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioBanco {
    Banco guardar(Banco banco);

    PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete, Banco banco);

    Banco buscarBanco(String email, String password);

    Banco buscarPorId(Long idBanco);

    PaqueteDeSangre buscarSangre(String s);

    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);

    List<BancoConTiposDeSangreDTO> obtenerLaCoincidenciaEnSangreDeTodosLosBancos(String sangreBuscada);

    List<BancoConTiposDeSangreDTO> obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos(String tipoProducto);

    List<BancoConTiposDeSangreDTO> obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(String sangreBuscada, String tipoProducto);

    PaqueteDeSangre buscarSangreXId(int paqueteId);

    ArrayList<Banco> searchBankByScore();

    ArrayList<Banco> searchBankByScoreAndBlood(String sangre);

    void actualizarBanco(Banco banco);

    List<Banco> obtenerTodosLosBancosNoVerificados();

    boolean activarBanco(Long id);

    boolean borrarBanco(Long id);
}
