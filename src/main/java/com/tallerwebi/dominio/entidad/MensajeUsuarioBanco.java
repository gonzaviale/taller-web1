package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class MensajeUsuarioBanco {

    private String mensaje;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Banco banco;

    private String emisor;

    private Boolean leido;
}
