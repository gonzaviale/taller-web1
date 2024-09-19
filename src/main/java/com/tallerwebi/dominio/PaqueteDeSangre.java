package com.tallerwebi.dominio;
import javax.persistence.*;
import java.util.Objects;


@Entity
    public class PaqueteDeSangre {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String tipoSangre;
        private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name ="Banco_id")
        private Banco banco;

        // Constructor por defecto
        public PaqueteDeSangre() {}

        public PaqueteDeSangre(String tipoSangre, int cantidad, Banco banco) {
            this.tipoSangre = tipoSangre;
            this.cantidad = cantidad;
            this.banco = banco;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaqueteDeSangre that = (PaqueteDeSangre) o;
        return cantidad == that.cantidad && Objects.equals(id, that.id) && Objects.equals(tipoSangre, that.tipoSangre) && Objects.equals(banco, that.banco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoSangre, cantidad, banco);
    }

    // Getters y setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTipoSangre() {
            return tipoSangre;
        }

        public void setTipoSangre(String tipoSangre) {
            this.tipoSangre = tipoSangre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public Banco getBanco() {
            return banco;
        }

        public void setBanco(Banco banco) {
            this.banco = banco;
        }
    }
