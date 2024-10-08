package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioMensajeUsuarioBanco")
public class RepositorioMensajeUsuarioBancoImpl implements RepositorioMensajeUsuarioBanco {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}


    @Override
    public MensajeUsuarioBanco crearMensaje(String mensaje, String emisor, Usuario usuario, Banco banco) {
        if(!mensaje.isEmpty() && !emisor.isEmpty() && usuario != null && banco != null) {
            MensajeUsuarioBanco mensajeUsuario = new MensajeUsuarioBanco();
            mensajeUsuario.setBanco(banco);
            mensajeUsuario.setMensaje(mensaje);
            mensajeUsuario.setEmisor(emisor);
            mensajeUsuario.setUsuario(usuario);
            sessionFactory.getCurrentSession().save(mensajeUsuario);
            return mensajeUsuario;
        } else {
            return null;
        }
    }

    @Override
    public Banco searchBankById(Long bancoId) {
        return (Banco) sessionFactory.getCurrentSession().createCriteria(Banco.class)
                .add(Restrictions.eq("id", bancoId))
                .uniqueResult();
    }

}
