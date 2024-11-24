package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;

@Repository("repositorioMensajeUsuarioUsuario")
public class RepositorioMensajeUsuarioUsuarioImpl implements RepositorioMensajeUsuarioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMensajeUsuarioUsuarioImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public MensajeUsuarioUsuario crearMensaje(String mensaje, Usuario usuarioEmisor, Usuario usuarioReceptor) {
        MensajeUsuarioUsuario mensajeUsuario = new MensajeUsuarioUsuario();
        mensajeUsuario.setMensaje(mensaje);
        mensajeUsuario.setUsuarioEmisor(usuarioEmisor);
        mensajeUsuario.setUsuarioReceptor(usuarioReceptor);
        mensajeUsuario.setFecha(new Date());
        mensajeUsuario.setLeido(false);
        sessionFactory.getCurrentSession().save(mensajeUsuario);
        return mensajeUsuario;
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByUser(Usuario usuario) {
        String hql = "SELECT m FROM MensajeUsuarioUsuario m " +
                "WHERE (m.usuarioEmisor = :usuario OR m.usuarioReceptor = :usuario) " +
                "AND (m.usuarioEmisor != m.usuarioReceptor)" +
                "AND m.fecha = (SELECT MAX(sub.fecha) FROM MensajeUsuarioUsuario sub " +
                "               WHERE sub.usuarioEmisor = m.usuarioEmisor OR sub.usuarioReceptor = m.usuarioReceptor) " +
                "ORDER BY m.fecha DESC";

        try {
            return (ArrayList<MensajeUsuarioUsuario>) sessionFactory.getCurrentSession()
                    .createQuery(hql, MensajeUsuarioUsuario.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch (HibernateException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByUserId(Long usuario) {
        return null;
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByUserUserIds(Long usuarioEmisor, Long usuarioReceptor) {
        return null;
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByUserUser(Usuario usuario1, Usuario usuario2) {
        String hql = "SELECT m FROM MensajeUsuarioUsuario m " +
        "WHERE (m.usuarioEmisor = :usuario1 AND m.usuarioReceptor = :usuario2) OR " +
                "(m.usuarioEmisor = :usuario2 AND m.usuarioReceptor = :usuario1)";
        try {
            return (ArrayList<MensajeUsuarioUsuario>) sessionFactory.getCurrentSession()
                    .createQuery(hql, MensajeUsuarioUsuario.class)
                    .setParameter("usuario1", usuario1)
                    .setParameter("usuario2", usuario2)
                    .getResultList();
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
