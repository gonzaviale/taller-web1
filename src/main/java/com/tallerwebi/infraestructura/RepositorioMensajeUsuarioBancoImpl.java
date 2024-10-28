package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;

@Repository("repositorioMensajeUsuarioBanco")
public class RepositorioMensajeUsuarioBancoImpl implements RepositorioMensajeUsuarioBanco {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMensajeUsuarioBancoImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}


    @Override
    public MensajeUsuarioBanco crearMensaje(String mensaje, String emisor, Usuario usuario, Banco banco) {
        if(!mensaje.isEmpty() && !emisor.isEmpty() && usuario != null && banco != null) {
            MensajeUsuarioBanco mensajeUsuario = new MensajeUsuarioBanco();
            mensajeUsuario.setBanco(banco);
            mensajeUsuario.setMensaje(mensaje);
            mensajeUsuario.setEmisor(emisor);
            mensajeUsuario.setUsuario(usuario);
            mensajeUsuario.setFecha(new Date());
            mensajeUsuario.setLeido(false);
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

    @Override
    public ArrayList<MensajeUsuarioBanco> getMessagesByUser(Usuario usuario) {
        String hql = "SELECT m FROM MensajeUsuarioBanco m " +
                "WHERE m.usuario = :usuario " +
                "AND m.fecha = (SELECT MAX(sub.fecha) FROM MensajeUsuarioBanco sub " +
                "               WHERE sub.banco = m.banco AND sub.usuario = m.usuario) " +
                "ORDER BY m.fecha DESC";

        try {
            return (ArrayList<MensajeUsuarioBanco>) sessionFactory.getCurrentSession()
                    .createQuery(hql, MensajeUsuarioBanco.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch (HibernateException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<MensajeUsuarioBanco> getMessagesByUserAndBank(Long usuario, Long banco) {
        try {
            Usuario usuarioObj = sessionFactory.getCurrentSession().get(Usuario.class, usuario);
            Banco bancoObj = sessionFactory.getCurrentSession().get(Banco.class, banco);

            return (ArrayList<MensajeUsuarioBanco>) sessionFactory.getCurrentSession()
                    .createCriteria(MensajeUsuarioBanco.class)
                    .add(Restrictions.eq("usuario", usuarioObj))
                    .add(Restrictions.eq("banco", bancoObj))
                    .list();
        } catch (RuntimeException e){
            return new ArrayList<>();
        }
    }

    @Override
    public Usuario searchUser(Long usuario) {
        try {
            return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                    .add(Restrictions.eq("id", usuario))
                    .uniqueResult();
        } catch (HibernateException e) {
            return null;
        }
    }


}
