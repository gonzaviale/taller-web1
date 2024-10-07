package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Felino")
public class Felino extends Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

}
