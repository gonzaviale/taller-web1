package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioMascota")
public class RepositorioMascotaImpl implements RepositorioMascota {

    private final SessionFactory sessionFactory;

    public RepositorioMascotaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Mascota> buscarMascotaEnRevision() {
        Session session = sessionFactory.openSession();
        List<Mascota> mascotasEnRevision = null;

        mascotasEnRevision = session.createCriteria(Mascota.class)
                .add(Restrictions.eq("enRevision", true))
                .addOrder(Order.desc("receptor"))
                .addOrder(Order.asc("id"))
                .list();

        return mascotasEnRevision;
    }

    @Override
    public void aprobarMascota(Long mascotaId) {
        Mascota mascota = (Mascota) sessionFactory.getCurrentSession()
                .createCriteria(Mascota.class)
                .add(Restrictions.eq("id", mascotaId))
                .uniqueResult();
        mascota.setRevision(false);
        mascota.setAprobado(true);
        sessionFactory.getCurrentSession().saveOrUpdate(mascota);
    }

    @Override
    public void rechazarMascota(Long mascotaId) {
        Mascota mascota = (Mascota) sessionFactory.getCurrentSession()
                .createCriteria(Mascota.class)
                .add(Restrictions.eq("id", mascotaId))
                .uniqueResult();
        mascota.setRevision(false);
        mascota.setRechazado(true);
        sessionFactory.getCurrentSession().saveOrUpdate(mascota);
    }

    @Override
    public List<Mascota> obtenerMascotasPorDueno(Usuario dueno) {
        List<Mascota> misMascotas =  sessionFactory.getCurrentSession()
                .createCriteria(Mascota.class)
                .add(Restrictions.eq("duenio", dueno))
                .list();
        return misMascotas;
    }

    @Override
    public Mascota buscarMascotaPorId(Long mascotaId) {
        Mascota mascota = (Mascota) sessionFactory.getCurrentSession()
                .createCriteria(Mascota.class)
                .add(Restrictions.eq("id", mascotaId))
                .uniqueResult();
        return mascota;
    }

    @Override
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo) {
        final Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Mascota.class);

        if (nombre != null && !nombre.isEmpty()) {
            criteria.add(Restrictions.like("nombre","%" +nombre + "%",MatchMode.ANYWHERE));
        }

        if (sangre != null && !sangre.isEmpty()) {
            criteria.add(Restrictions.like("sangre", "%" +sangre + "%", MatchMode.ANYWHERE));
        }

        if (tipo != null && !tipo.isEmpty()) {
            criteria.add(Restrictions.like("tipo", "%" +tipo + "%",MatchMode.ANYWHERE));
        }

        List<Mascota> mascotas = criteria.list();
        return new ArrayList<>(mascotas);
    }


    @Override
    public void agregarMascota(Mascota mascota) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(mascota);
    }

    @Override
    public Boolean eliminarMascota(Mascota mascota) {

        Criteria publicacionesCriteria = sessionFactory.getCurrentSession().createCriteria(Publicacion.class)
                .add(Restrictions.eq("mascotaDonante.id", mascota.getId())); // Filtro por el ID de la mascota

        boolean existeEnPublicaciones = !publicacionesCriteria.list().isEmpty();

        // Crea el criterio para verificar en solicitudes
        Criteria solicitudesDonantesCriteria =sessionFactory.getCurrentSession().createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("mascotaDonante.id", mascota.getId())); // Filtro por el ID de la mascota

        boolean existeEnSolicitudesDonantes = !solicitudesDonantesCriteria.list().isEmpty();

        // Crea el criterio para verificar en solicitudes
        Criteria solicitudesReceptorCriteria =sessionFactory.getCurrentSession().createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("mascotaReceptora.id", mascota.getId())); // Filtro por el ID de la mascota

        boolean existeEnSolicitudesReceptora = !solicitudesReceptorCriteria.list().isEmpty();

        // Retorna verdadero si está asociada a alguna de las dos entidades
        if( existeEnPublicaciones || existeEnSolicitudesDonantes || existeEnSolicitudesReceptora){
            return Boolean.FALSE;
        }
        sessionFactory.getCurrentSession().delete(mascota);

        return Boolean.TRUE;
    }

    @Override
    public void actualizarMascota(Mascota mascota) {
        final Session session = sessionFactory.getCurrentSession();

        Mascota encontrada=this.buscarMascotaPorId(mascota.getId());

        asignarDatosAMascota(mascota,encontrada);

        session.update(encontrada);
    }

    private void asignarDatosAMascota(Mascota mascota, Mascota encontrada) {

        encontrada.setNombre(mascota.getNombre());
        encontrada.setPeso(mascota.getPeso());
        encontrada.setAnios(mascota.getAnios());
        encontrada.setTipo(mascota.getTipo());
        encontrada.setSangre(mascota.getSangre());

    }
}
