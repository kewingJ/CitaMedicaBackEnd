package com.example.citasmedicasbackbean.utils;

import com.example.citasmedicasbackbean.modelo.Cita;
import com.example.citasmedicasbackbean.modelo.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidarGuardarCita {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    public String validar(Usuario usuarioPaciente,
                               Usuario usuarioDoctor,
                               String fechaCita,
                               Date fechaCitaQuery){
        List listaResultado = new ArrayList<>();
        String mensaje = "";

        //primera validacion
        //Un paciente solo puede reservar una hora al día con un médico.
        String jpql1 = "SELECT c FROM Cita c where c.usuarioPaciente.idUsuario = :idPaciente AND DATE(c.fechaCita) = :fechaCita";
        TypedQuery<Cita> query1 = em.createQuery(jpql1, Cita.class);
        query1.setParameter("idPaciente", usuarioPaciente.getIdUsuario());
        query1.setParameter("fechaCita", fechaCitaQuery);
        listaResultado = query1.getResultList();
        if (listaResultado.size() > 0){
            mensaje = "Usted ya tiene una cita agendada para esa fecha";
        }

        //segunda validacion
        //Una cita no se puede superponer.
        String jpql2 = "SELECT c FROM Cita c where c.usuarioDoctor.idUsuario = :idDoctor AND c.fechaCita = :fechaCita";
        TypedQuery<Cita> query2 = em.createQuery(jpql2, Cita.class);
        query2.setParameter("idDoctor", usuarioDoctor.getIdUsuario());
        query2.setParameter("fechaCita", fechaCita);
        listaResultado = query2.getResultList();
        if (listaResultado.size() > 0){
            mensaje = "El doctor tiene una cita a esa hora, por favor seleccione otra hora";
        }

        //tercera validacion
        //Un médico puede ser agendado hasta 6 veces en un día.
        String jpql3 = "SELECT c FROM Cita c where c.usuarioDoctor.idUsuario = :idDoctor AND DATE(c.fechaCita) = :fechaCita";
        TypedQuery<Cita> query3 = em.createQuery(jpql3, Cita.class);
        query3.setParameter("idDoctor", usuarioDoctor.getIdUsuario());
        query3.setParameter("fechaCita", fechaCitaQuery);
        listaResultado = query3.getResultList();
        if (listaResultado.size() == 6){
            mensaje = "El doctor ya tiene agendado esa fecha, por favor seleccione otra fecha";
        }

        return mensaje;
    }
}
