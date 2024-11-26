package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioTurnoTransfusion;
import com.tallerwebi.dominio.entidad.TurnoTransfusion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("RepositorioTurnoTransfusion")
public class RepositorioTurnoTransfusionImpl implements RepositorioTurnoTransfusion {
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioTurnoTransfusionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarTurno(TurnoTransfusion turno) {
        sessionFactory.getCurrentSession().save(turno);
    }
}
