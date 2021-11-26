package at.ac.fhcampuswien.usermanagement;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @GET
    @Path("/{name}")
    @Produces("text/plain")
    public String hello(@PathParam("name") String name) {
        return "Hello " + name;
    }
    @GET
    @Path("/{username}/{password}")
    @Produces("text/plain")
    public String login(@PathParam("username")String username, @PathParam("password")String password) {
        return username + password;

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public String register(NewUserDTO newUserDTO) {

        return newUserDTO.getLastname();
    }

}