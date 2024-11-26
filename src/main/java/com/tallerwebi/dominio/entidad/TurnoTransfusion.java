package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TurnoTransfusion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaYHora;

    @ManyToOne
    private SolicitudAUnaPublicacion solicitudAUnaPublicacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public SolicitudAUnaPublicacion getSolicitudAUnaPublicacion() {
        return solicitudAUnaPublicacion;
    }

    public void setSolicitudAUnaPublicacion(SolicitudAUnaPublicacion solicitudAUnaPublicacion) {
        this.solicitudAUnaPublicacion = solicitudAUnaPublicacion;
    }
}
