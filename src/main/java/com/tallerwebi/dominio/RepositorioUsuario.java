package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);

    Usuario buscarPorId(Long id);

    void modificar(Usuario usuario);

    List<Usuario> obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

    List<Usuario> obtenerTodosLosVeterinariosVerificados();

    List<Usuario> obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada(String sangreBuscada);

    List<Usuario> obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(String sangreBuscada);
}

