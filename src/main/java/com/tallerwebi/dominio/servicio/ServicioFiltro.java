package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import com.tallerwebi.presentacion.DTO.UsuarioFiltradoDTO;

import java.util.ArrayList;
import java.util.List;

public interface ServicioFiltro {

    ArrayList<Mascota> consultarMascota(String nombre, String sangre, String tipo);
    ArrayList<Publicacion> consultarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion);

    ArrayList<BancoConTiposDeSangreDTO> obtenerCoincidenciasEnBancosDeSangre(String sangreBuscada, String tipoProducto);

    List<Usuario> obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

    List<UsuarioFiltradoDTO> obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(String sangreBuscada, String tipoDeBusqueda);

    List<Usuario> obtenerTodosLosVeterinariosVerificados();

    List<Usuario> obtenerTodosLosVeterinariosNoVerificados();

    boolean activarUsuarioBuscadoPor(Long id);

    boolean desactivarUsuarioBuscadoPor(Long id);

    List<Banco> obtenerTodosLosBancosNoVerificados();
}