package com.example.citasmedicasbackbean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/schedule")
public class ServicioDoctor {
    @GET
    @Produces("text/plain")
    public String hello() {
        Client cliente = ClientBuilder.newClient();
        List<Doctor> doctors = cliente.target("https://api.jsonbin.io/b/61b3b51e62ed886f915dd68a").request(MediaType.APPLICATION_JSON).get(new GenericType<List<Doctor>>() {});
        return "saludo";
    }
}
