package com.example.citasmedicasbackbean.utils;

import com.example.citasmedicasbackbean.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;

public class ValidarRolUsuario {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    public String obtenerRolUsuario(String username, String password){
        String rolUsuario = "";
        Usuario usuario = null;
        try {
            String user = username;
            String pass = password;

            String jpql = "SELECT u FROM Usuario u where u.username = :user";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("user", user);
            if (query.getSingleResult() != null){
                if(BCrypt.checkpw(pass, query.getSingleResult().getPassword())){
                    usuario = query.getSingleResult();
                    rolUsuario = usuario.getRol();
                }
            }
        } catch (
        NoResultException ex) {
            ex.printStackTrace();
        }
        return rolUsuario;
    }
}
