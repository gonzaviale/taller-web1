package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.BancoExistente;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

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
    public void registrarBanco(Banco banco) throws BancoExistente {
        Banco bancoEncontrado = repositorioUsuario.buscarBanco(banco.getEmail(), banco.getPassword());
        if(bancoEncontrado != null){
            throw new BancoExistente();
        }
        repositorioUsuario.guardarBanco(banco);
    }

    @Override
    public Banco consultarBanco(String email, String password) {
        return repositorioUsuario.buscarBanco(email, password);
    }

}



