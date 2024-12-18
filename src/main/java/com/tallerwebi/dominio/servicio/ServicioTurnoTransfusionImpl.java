package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioTurnoTransfusion;
import com.tallerwebi.dominio.entidad.TurnoTransfusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("ServicioTurnoTransfusion")
@Transactional
public class ServicioTurnoTransfusionImpl implements ServicioTurnoTransfusion{
    private final RepositorioTurnoTransfusion repositorioTurnoTransfusion;
    private final LocalSessionFactoryBean sessionFactory;

    @Autowired
    public ServicioTurnoTransfusionImpl(RepositorioTurnoTransfusion repositorioTurnoTransfusion, LocalSessionFactoryBean sessionFactory) {
        this.repositorioTurnoTransfusion = repositorioTurnoTransfusion;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TurnoTransfusion> traerTurnosVigentesVet(Long id) {
        List<TurnoTransfusion> turnos = repositorioTurnoTransfusion.traerTodosLosTurnos();
        return turnos.stream()
                .filter(turno -> turno.getFechaYHora().isAfter(LocalDateTime.now())
                && turno.getSolicitudAUnaPublicacion().getVeterinario().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public void guardarTurno(TurnoTransfusion turno) {
        repositorioTurnoTransfusion.guardarTurno(turno);
    }

    @Override
    public List<TurnoTransfusion> traerTurnosVigentesReceptor(Long id) {
            List<TurnoTransfusion> turnos = repositorioTurnoTransfusion.traerTodosLosTurnos();
            return turnos.stream()
                    .filter(turno -> turno.getFechaYHora().isAfter(LocalDateTime.now())
                            && turno.getSolicitudAUnaPublicacion().getMascotaReceptora().getDuenio().getId() == id)
                    .collect(Collectors.toList());
    }

    @Override
    public List<TurnoTransfusion> traerTurnosVigentesDonante(Long id) {
        List<TurnoTransfusion> turnos = repositorioTurnoTransfusion.traerTodosLosTurnos();
        return turnos.stream()
                .filter(turno -> turno.getFechaYHora().isAfter(LocalDateTime.now())
                        && turno.getSolicitudAUnaPublicacion().getMascotaDonante().getDuenio().getId() == id)
                .collect(Collectors.toList());
    }
}
