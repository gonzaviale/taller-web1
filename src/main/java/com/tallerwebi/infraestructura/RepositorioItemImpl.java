package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Item;
import com.tallerwebi.dominio.RepositorioItem;
import com.tallerwebi.dominio.TipoItem;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioItemImpl implements RepositorioItem {

    private SessionFactory sessionFactory;

    public RepositorioItemImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Item item) {
        this.sessionFactory.getCurrentSession().save(item);
    }

    @Override
    public List<Item> obtenerPorTipoItem(TipoItem tipoItem) {

        String hql = "FROM Item WHERE tipoItem = :tipoItem";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("tipoItem", tipoItem);

        return query.getResultList();
    }

    @Override
    public void actualizar(Item item) {
//        this.sessionFactory.getCurrentSession().saveOrUpdate(item);
        String hql = "UPDATE Item set descripcion = :descripcion WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("descripcion", item.getDescripcion());
        query.setParameter("id", item.getId());
        query.executeUpdate(); // Sirve tambien para delete
    }
}
