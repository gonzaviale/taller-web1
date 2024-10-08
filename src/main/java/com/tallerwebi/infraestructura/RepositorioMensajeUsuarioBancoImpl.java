package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioBanco;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioMensajeUsuarioBanco")
public class RepositorioMensajeUsuarioBancoImpl implements RepositorioMensajeUsuarioBanco {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}


}
