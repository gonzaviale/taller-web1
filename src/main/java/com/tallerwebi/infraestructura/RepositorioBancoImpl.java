package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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


    public List<Banco> getAllBanco() {
        String hql = "FROM Banco"; // Traer todos los registros de la entidad Publicacion
        Query<Banco> query = sessionFactory.getCurrentSession().createQuery(hql, Banco.class);
        return query.getResultList();
    }

    @Override
    public List<BancoConTiposDeSangre> obtenerLaCoincidenciaEnSangreDeTodosLosBancos(String sangreBuscada){

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
    public List<BancoConTiposDeSangre> obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos(String sangreBuscada ,String tipoProducto) {

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

    private static BancoConTiposDeSangre getBancoConTiposDeSangre(Banco banco, PaqueteDeSangre paquete) {
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
