package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;

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

    void actualizarUsuario(Usuario usuarioEnSesion);

    List<Mascota> obtenerMascotaDelUsuario(Long id);

    List<Publicacion> obtenerPublicacionesDelUsuario(Long id);

    Banco buscarBanco(String email, String password);
}

