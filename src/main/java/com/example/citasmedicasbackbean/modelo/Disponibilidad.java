package com.example.citasmedicasbackbean.modelo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad implements Serializable {
    private static final long serialVersionUID = -1221718121500357998L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidad")
    private long idDisponibilidad;

    @ManyToOne
    @JoinColumn(name = "id_doctor", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Column(name = "dia")
    private String dia;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    public Disponibilidad() {
    }

    public Disponibilidad(Usuario usuario, String dia, String horaInicio, String horaFin) {
        this.usuario = usuario;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public long getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(long idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
