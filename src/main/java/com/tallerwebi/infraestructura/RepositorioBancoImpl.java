package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("RepositorioBanco")
public class RepositorioBancoImpl implements RepositorioBanco {

    private SessionFactory sessionFactory;

    public RepositorioBancoImpl(org.hibernate.SessionFactory sessionFactory) {

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
    public ArrayList<Banco> searchBankByScore(){
        return (ArrayList<Banco>) sessionFactory.getCurrentSession().createCriteria(Banco.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public ArrayList<Banco> searchBankByScoreAndBlood(String sangre){
        return (ArrayList<Banco>) sessionFactory.getCurrentSession().createCriteria(Banco.class)
                .createAlias("stockSangre", "s")
                .add(Restrictions.eq("s.tipoSangre", sangre))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public void actualizarBanco(Banco banco){
        sessionFactory.getCurrentSession().update(banco);
    }

    @Override
    public void guardarCampania(Campana campana, Banco banco) {
        this.actualizarBanco(banco);
        sessionFactory.getCurrentSession().save(campana);

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

    @Override
    public List<Solicitud> solicitudesPorBanco(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Solicitud.class)
                .add(Restrictions.eq("bancoId", idBanco))
                .list();
    }

    @Override
    public Solicitud guardarSolicitud(Solicitud solicitud1) {
        sessionFactory.getCurrentSession().save(solicitud1);
        return solicitud1;
    }

    @Override
    public Solicitud buscarSolicitudPorId(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Solicitud> cq = cb.createQuery(Solicitud.class);
        Root<Solicitud> root = cq.from(Solicitud.class);
        cq.select(root).where(cb.equal(root.get("id"), id));
        return session.createQuery(cq).uniqueResult();
    }

    @Override
    public List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatible(Solicitud solicitud) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);

        Predicate tipoProductoPredicate = cb.equal(root.get("tipoProducto"), solicitud.getTipoProducto());
        Predicate tipoSangrePredicate = cb.equal(root.get("tipoSangre"), solicitud.getTipoSangre());
        Predicate cantidadPredicate = cb.greaterThanOrEqualTo(root.get("cantidad"), solicitud.getCantidad());
        Predicate bancoIdPredicate = cb.equal(root.get("banco").get("id"), solicitud.getBancoId()); // Modificado aquí

        cq.select(root).where(cb.and(tipoProductoPredicate, tipoSangrePredicate, cantidadPredicate, bancoIdPredicate));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public void rechazarSolicitud(int solicitudId) {
        Session session = sessionFactory.getCurrentSession();


        Solicitud solicitud = this.buscarSolicitudPorId(solicitudId);
        if (solicitud != null) {

            solicitud.setEstado("Rechazada");

            session.update(solicitud);
        }
    }

    @Override
    public PaqueteDeSangre buscarSangreXId(int paqueteId) {
        Session session = this.sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);

        Predicate paqueteIdPredicate = cb.equal(root.get("id"), paqueteId);

        cq.select(root).where(paqueteIdPredicate);

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public void solicitudAprobar(int solicitudId) {

        Session session = sessionFactory.getCurrentSession();
        Solicitud solicitud = this.buscarSolicitudPorId(solicitudId);
        if (solicitud != null) {
            solicitud.setEstado("aprobada");
            session.update(solicitud);
        }


    }


    public List<Banco> getAllBanco() {
        String hql = "FROM Banco"; // Traer todos los registros de la entidad Publicacion
        Query<Banco> query = sessionFactory.getCurrentSession().createQuery(hql, Banco.class);
        return query.getResultList();
    }

    @Override
    public List<BancoConTiposDeSangre> obtenerLaCoincidenciaEnSangreDeTodosLosBancos(String sangreBuscada) {

        //necesito un lugar donde almacenar los resultados
        List<BancoConTiposDeSangre> resultados = new ArrayList<>();

        //obtengo todos los bancos
        List<Banco> bancos = getAllBanco();

        //recorro todos los bancos
        for (Banco banco : bancos) {
            //recorro los paquetes de el banco del que este en iteracion
            for (PaqueteDeSangre paquete : banco.getPaquetesDeSangre()) {
                //si el paquete que contiene es igual
                if (paquete.getTipoSangre().contains(sangreBuscada)) {
                    BancoConTiposDeSangre bancoConTipos = getBancoConTiposDeSangre(banco, paquete);
                    //guardo el objeto
                    resultados.add(bancoConTipos);
                }
            }

        }

        return resultados;
    }

    @Override
    public List<BancoConTiposDeSangre> obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos(String tipoProducto) {
        //necesito un lugar donde almacenar los resultados
        List<BancoConTiposDeSangre> resultados = new ArrayList<>();

        //obtengo todos los bancos
        List<Banco> bancos = getAllBanco();

        //recorro todos los bancos
        for (Banco banco : bancos) {
            //recorro los paquetes de el banco del que este en iteracion
            for (PaqueteDeSangre paquete : banco.getPaquetesDeSangre()) {
                //si el paquete que contiene es igual
                if (paquete.getTipoProducto().contains(tipoProducto)) {
                    BancoConTiposDeSangre bancoConTipos = getBancoConTiposDeSangre(banco, paquete);
                    //guardo el objeto
                    resultados.add(bancoConTipos);
                }
            }

        }

        return resultados;
    }

    @Override
    public List<BancoConTiposDeSangre> obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(String sangreBuscada, String tipoProducto) {

        //necesito un lugar donde almacenar los resultados
        List<BancoConTiposDeSangre> resultados = new ArrayList<>();

        //obtengo todos los bancos
        List<Banco> bancos = getAllBanco();

        //recorro todos los bancos
        for (Banco banco : bancos) {
            //recorro los paquetes de el banco del que este en iteracion
            for (PaqueteDeSangre paquete : banco.getPaquetesDeSangre()) {
                //si el paquete que contiene es igual
                if (paquete.getTipoSangre().contains(sangreBuscada) && paquete.getTipoProducto().contains(tipoProducto)) {
                    BancoConTiposDeSangre bancoConTipos = getBancoConTiposDeSangre(banco, paquete);
                    //guardo el objeto
                    resultados.add(bancoConTipos);
                }
            }

        }

        return resultados;
    }

    private BancoConTiposDeSangre getBancoConTiposDeSangre(Banco banco, PaqueteDeSangre paquete) {
        BancoConTiposDeSangre bancoConTipos = new BancoConTiposDeSangre();
        //datos banco a guardar en el objeto
        bancoConTipos.setBancoId(banco.getId());
        bancoConTipos.setNombreBanco(banco.getNombreBanco());
        bancoConTipos.setDireccion(banco.getDireccion());
        bancoConTipos.setTelefono(banco.getTelefono());
        bancoConTipos.setCiudad(banco.getCiudad());
        bancoConTipos.setEmail(banco.getEmail());
        //datos de sangre a guardar en el objeto
        bancoConTipos.setTipoSangre(paquete.getTipoSangre());
        bancoConTipos.setSangreId(paquete.getId());
        bancoConTipos.setTipoProducto(paquete.getTipoProducto());
        bancoConTipos.setCantidad(paquete.getCantidad());
        return bancoConTipos;
    }

}

