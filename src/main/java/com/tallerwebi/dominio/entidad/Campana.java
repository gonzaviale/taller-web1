package com.tallerwebi.dominio.entidad;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Campana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String ubicacion;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;

    public Campana() {
    }


    public Campana(String nombre, LocalDate fechaInicio, LocalDate fechaFin, String ubicacion, String descripcion, Banco banco) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.banco = banco;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public String toString() {
        return "Campana{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", ubicacion='" + ubicacion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", banco=" + banco + // Imprimir detalles del banco
                '}';
    }
}
