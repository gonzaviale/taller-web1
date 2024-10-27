package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@DiscriminatorValue("duenoMascota")
public class DuenoMascota extends Usuario{
}