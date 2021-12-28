package com.example.citasmedicasbackbean.modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "citas")
public class Cita implements Serializable {
    private static final long serialVersionUID = -1221718121500357998L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private long idCita;

    @ManyToOne
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_usuario")
    private Usuario usuarioPaciente;

    @ManyToOne
    @JoinColumn(name = "id_doctor", referencedColumnName = "id_usuario")
    private Usuario usuarioDoctor;

    @Column(name = "fecha_cita")
    private String fechaCita;

    public Cita() {
    }

    public Cita(Usuario usuarioPaciente, Usuario usuarioDoctor, String fechaCita) {
        this.usuarioPaciente = usuarioPaciente;
        this.usuarioDoctor = usuarioDoctor;
        this.fechaCita = fechaCita;
    }

    public long getIdCita() {
        return idCita;
    }

    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    public Usuario getUsuarioPaciente() {
        return usuarioPaciente;
    }

    public void setUsuarioPaciente(Usuario usuarioPaciente) {
        this.usuarioPaciente = usuarioPaciente;
    }

    public Usuario getUsuarioDoctor() {
        return usuarioDoctor;
    }

    public void setUsuarioDoctor(Usuario usuarioDoctor) {
        this.usuarioDoctor = usuarioDoctor;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }
}
