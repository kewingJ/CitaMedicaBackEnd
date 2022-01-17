package com.example.citasmedicasbackbean.service;

import com.example.citasmedicasbackbean.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import javax.persistence.*;
import javax.ws.rs.core.Response;

public class AuthService {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    /**
     * metodo para registrar un nuevo usuario de tipo paciente
     * @param usuarioRequest
     * @return retona respuesta de que se creo el usuario
     */
    public Response signup(Usuario usuarioRequest) {
        String nombre = usuarioRequest.getNombre();
        String username = usuarioRequest.getUsername();
        String rol = usuarioRequest.getRol();
        //encriptar con jbcrypt
        String password = BCrypt.hashpw(usuarioRequest.getPassword(), BCrypt.gensalt(10));

        //pasar datos al modelo
        Usuario usuario = new Usuario(nombre, username, password, rol);
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }

    /**
     * metodo para verificar login de un usuario
     * @param usuarioRequest
     * @return retona datos del usuario
     */
    public Response login(Usuario usuarioRequest) {
        Usuario usuario = null;
        try {
            String user = usuarioRequest.getUsername();
            String pass = usuarioRequest.getPassword();

            String jpql = "SELECT u FROM Usuario u where u.username = :user";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("user", user);
            if (query.getSingleResult() != null){
                if(BCrypt.checkpw(pass, query.getSingleResult().getPassword())){
                    usuario = query.getSingleResult();
                }
            }
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return Response.ok(usuario).build();
    }
}
