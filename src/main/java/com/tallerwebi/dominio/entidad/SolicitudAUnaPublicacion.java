package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class SolicitudAUnaPublicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean aprobada;

    @ManyToOne
    private Mascota mascotaDonante;

    @ManyToOne
    private Mascota mascotaReceptora;

    public Mascota getMascotaReceptora() {
        return mascotaReceptora;
    }

    public void setMascotaReceptora(Mascota mascotaReceptora) {
        this.mascotaReceptora = mascotaReceptora;
    }

    public Mascota getMascotaDonante() {
        return mascotaDonante;
    }

    public void setMascotaDonante(Mascota mascotaDonante) {
        this.mascotaDonante = mascotaDonante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAprobada() {
        return aprobada;
    }

    public void setAprobada(Boolean aprobada) {
        this.aprobada = aprobada;
    }
}
