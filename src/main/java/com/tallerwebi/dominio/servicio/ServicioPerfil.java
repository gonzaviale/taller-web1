package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioPerfil {

    Usuario buscarUsuarioPorId(Long id);

    void actualizarUsuario(Usuario usuarioEnSesion);

    List<Mascota> obtenerMascotasDelUsuario(Long id);

    List<Publicacion> obtenerPublicacionesDelUsuario(Long id);
}
