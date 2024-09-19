package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository("RepositorioBanco")
public class RepositorioBancoImpl implements RepositorioBanco {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioBancoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Banco guardar(Banco banco) {
        sessionFactory.getCurrentSession().save(banco);
        return banco;
    }

    @Override
    public PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete, Banco banco) {
        banco.agregarPaqueteDeSangre(paquete);

        sessionFactory.getCurrentSession().save(paquete);
        sessionFactory.getCurrentSession().saveOrUpdate(banco);
        return paquete;
    }

    @Override
    public Banco buscarBanco(String email, String password) {
        final Session session = sessionFactory.getCurrentSession();
        return (Banco) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public Banco buscarPorId(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Banco> cq = cb.createQuery(Banco.class);
        Root<Banco> root = cq.from(Banco.class);
        cq.select(root).where(cb.equal(root.get("id"), idBanco));
        return session.createQuery(cq).uniqueResult();
    }



    @Override
    public PaqueteDeSangre buscarSangre(String tipoSangre) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);
        cq.select(root).where(cb.equal(root.get("tipoSangre"), tipoSangre));
        return session.createQuery(cq).uniqueResult();
    }




    public List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);


        cq.select(root);
        cq.where(cb.equal(root.get("banco").get("id"), idBanco));

        return session.createQuery(cq).getResultList();
    }

}
