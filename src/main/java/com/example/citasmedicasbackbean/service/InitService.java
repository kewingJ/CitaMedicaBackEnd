package com.example.citasmedicasbackbean.service;

import com.example.citasmedicasbackbean.modelo.Usuario;
import com.example.citasmedicasbackbean.utils.Disponibilidad;
import com.example.citasmedicasbackbean.utils.Doctor;
import org.mindrot.jbcrypt.BCrypt;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class InitService {

    public Response iniciar() {
        Client cliente = ClientBuilder.newClient();
        List<Doctor> doctors = cliente.target("https://api.jsonbin.io/b/61b3b51e62ed886f915dd68a").request(MediaType.APPLICATION_JSON).get(new GenericType<List<Doctor>>() {});

        for (Doctor doctor : doctors)
        {
            //Datos a guardar
            String nombre = doctor.getNombre();

            //crear username a base del nombre y apellido
            //ejemplo kewing jarquin a kjarquin
            String[] parts = nombre.split(" ");
            String name = parts[0];
            char caracterNombre = name.charAt(0);
            String apellido = parts[1];
            String username = caracterNombre+""+apellido;

            //encriptar con jbcrypt
            String password = BCrypt.hashpw("holamundo", BCrypt.gensalt(10));
            String rol = "doctor";

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
            EntityManager em = emf.createEntityManager();
            //pasar datos al modelo
            Usuario usuario = new Usuario(nombre, username.toLowerCase(), password, rol);
            try {
                em.getTransaction().begin();
                em.persist(usuario);
                em.getTransaction().commit();
                //guardar en la tabla de disponibilidad
                List<Disponibilidad> disponibilidads = doctor.getDisponibilidad();
                for (Disponibilidad calendario : disponibilidads)
                {
                    com.example.citasmedicasbackbean.modelo.Disponibilidad disponibilidad = new com.example.citasmedicasbackbean.modelo.Disponibilidad(usuario, calendario.getDia(), calendario.getHoraInicio(), calendario.getHoraFin());
                    em.getTransaction().begin();
                    em.persist(disponibilidad);
                    em.getTransaction().commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }

    public Response bienvenida() {
        return Response.ok("Bienvenido").build();
    }
}
