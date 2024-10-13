package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service ("servicioPerfi")
@Transactional
public class ServicioPerfilImpl implements ServicioPerfil {

    RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioPerfilImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
    }

    @Override
    public void actualizarUsuario(Usuario usuarioEnSesion) {
        repositorioUsuario.actualizarUsuario(usuarioEnSesion);
    }

    @Override
    public List<Mascota> obtenerMascotasDelUsuario(Long id){
        List<Mascota> mascotas= repositorioUsuario.obtenerMascotaDelUsuario(id);

        mascotas.sort(Comparator.comparing(Mascota::isRechazado)
                .thenComparing(Mascota::isEnRevision)
                .thenComparing(Mascota::isAprobado));

        return mascotas;
    }

    @Override
    public List<Publicacion> obtenerPublicacionesDelUsuario(Long id) {
        List<Publicacion> publicaciones = repositorioUsuario.obtenerPublicacionesDelUsuario(id);

        publicaciones.sort(Comparator
                .comparing(Publicacion::getEstaActiva)
                .thenComparing(Publicacion::getLocalDateTime).reversed());

        return publicaciones;
    }

}
