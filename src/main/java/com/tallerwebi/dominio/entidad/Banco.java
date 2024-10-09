package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Banco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_banco", nullable = false, length = 255)
    private String nombreBanco;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "ciudad", nullable = false, length = 255)
    private String ciudad;

    @Column(name = "pais", nullable = false, length = 255)
    private String pais;

    @Column(name = "telefono", nullable = false, length = 255)
    private String telefono;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "horario", nullable = false, length = 255)
    private String horario;

    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @OneToMany(mappedBy = "banco", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PaqueteDeSangre> stockSangre;





    public Banco(String nombreBanco, String direccion, String ciudad, String pais, String telefono, String email, String password, String horario) {
        this.nombreBanco = nombreBanco;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.horario = horario;
        this.stockSangre  = new ArrayList<>();
        this.puntos = 0;

    }

    public Banco() {
        this.stockSangre  = new ArrayList<>();
        this.puntos = 0;
    }



    public List<PaqueteDeSangre> getPaquetesDeSangre() {
        return stockSangre;
    }


    public void agregarPaqueteDeSangre(PaqueteDeSangre nuevoPaquete) {

        this.stockSangre.add(nuevoPaquete);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getPuntos() { return puntos; }

    public void setPuntos(Integer puntos) { this.puntos = puntos; }



    // Método para mostrar información del banco (opcional)
    @Override
    public String toString() {
        return "Banco{" +
                "id=" + id +
                ", nombreBanco='" + nombreBanco + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +  // Incluye la contraseña en toString si es necesario
                ", horario='" + horario + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banco banco = (Banco) o;
        return Objects.equals(id, banco.id) && Objects.equals(nombreBanco, banco.nombreBanco) && Objects.equals(direccion, banco.direccion) && Objects.equals(ciudad, banco.ciudad) && Objects.equals(pais, banco.pais) && Objects.equals(telefono, banco.telefono) && Objects.equals(email, banco.email) && Objects.equals(password, banco.password) && Objects.equals(horario, banco.horario) && Objects.equals(stockSangre, banco.stockSangre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreBanco, direccion, ciudad, pais, telefono, email, password, horario, stockSangre);
    }


    public void agregarCampania(Campana nuevaCampana) {
    }
}
