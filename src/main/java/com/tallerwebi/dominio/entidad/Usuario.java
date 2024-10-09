package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String password;
    private String rol;
    private Boolean activo = false;

    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Mascota> mascotas = new ArrayList<>();

    @OneToMany(mappedBy = "duenioPublicacion", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List <Publicacion> publicaciones = new ArrayList<>();


    // Métodos para gestionar la relación bidireccional
    public void agregarPublicaciones(Publicacion publicacion) {
        publicaciones.add(publicacion);
        publicacion.setDuenioPublicacion(this);
    }

    public void removerPublicaciones(Publicacion publicacion) {
        publicaciones.remove(publicacion);
        publicacion.setDuenioPublicacion(null);
    }

    // Métodos para gestionar la relación bidireccional
    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
        mascota.setDuenio(this);
    }

    public void removerMascota(Mascota mascota) {
        mascotas.remove(mascota);
        mascota.setDuenio(null);
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}