package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@DiscriminatorValue("duenoMascota")
public class DuenoMascota extends Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    Usuario usuario;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}