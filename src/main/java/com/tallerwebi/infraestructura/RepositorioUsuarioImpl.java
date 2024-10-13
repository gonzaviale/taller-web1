package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.Veterinario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public Usuario buscarPorId(Long id){
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta() {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT DISTINCT u FROM Usuario u " +
                "LEFT JOIN u.mascotas m " +
                "LEFT JOIN u.publicaciones p " +
                "WHERE (m IS NOT NULL OR p IS NOT NULL)";

        Query<Usuario> query = session.createQuery(hql, Usuario.class);

        return query.getResultList();
    }

    @Override
    public List<Usuario> obtenerTodosLosVeterinariosVerificados() {
        return sessionFactory.getCurrentSession().createCriteria(Veterinario.class)
                .add(Restrictions.eq("rol","veterinario")).list();
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada(String sangreBuscada) {
        return sessionFactory.getCurrentSession().createCriteria(Usuario.class,"usuario")
                .createAlias("usuario.mascotas", "mascota")
                .add(Restrictions.like("mascota.sangre", "%" + sangreBuscada + "%"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(String sangreBuscada) {
        return sessionFactory.getCurrentSession().createCriteria(Usuario.class,"usuario")
                .createAlias("usuario.publicaciones", "publicacion")
                .add(Restrictions.like("publicacion.tipoDeSangre", "%" + sangreBuscada + "%"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void actualizarUsuario(Usuario usuarioEnSesion) {
        sessionFactory.getCurrentSession().update(usuarioEnSesion);
    }

    @Override
    public List<Mascota> obtenerMascotaDelUsuario(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Mascota.class,"mascota")
                .add(Restrictions.eq("mascota.duenio.id",id))
                .list();
    }

    @Override
    public List<Publicacion> obtenerPublicacionesDelUsuario(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Publicacion.class,"publicacion")
                .add(Restrictions.eq("publicacion.duenioPublicacion.id",id))
                .list();
    }

}
