package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class SolicitudAUnaPublicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean aprobada;
    private Boolean pendiente;
    private Boolean rechazada;
    private Boolean vista;

    @ManyToOne
    private Mascota mascotaDonante;

    @ManyToOne
    private Mascota mascotaReceptora;

    @ManyToOne
    private Publicacion publicacion;

    @ManyToOne
    private Usuario veterinario;

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

    public Boolean getPendiente() {
        return pendiente;
    }

    public void setPendiente(Boolean pendiente) {
        this.pendiente = pendiente;
    }

    public Boolean getRechazada() {
        return rechazada;
    }

    public void setRechazada(Boolean rechazada) {
        this.rechazada = rechazada;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Usuario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Usuario veterinario) {
        this.veterinario = veterinario;
    }

    public Boolean getVista() {
        return vista;
    }

    public void setVista(Boolean vista) {
        this.vista = vista;
    }
}
