package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    final private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public Banco ConsultarBanco(String email, String password) {
        return repositorioUsuario.buscarBanco(email, password);
    }

    @Override
    public void RegistrarBanco(Banco banco) {
        repositorioUsuario.guardarBanco(banco);
    }
}