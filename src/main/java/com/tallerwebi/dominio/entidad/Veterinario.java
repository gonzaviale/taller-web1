package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@DiscriminatorValue("veterinario")
public class Veterinario extends Usuario{

    private String matricula;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
