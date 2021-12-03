package at.ac.fhcampuswien.usermanagement;

import at.ac.fhcampuswien.usermanagement.infrastructure.database.UserService;
import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public Response register(NewUserDTO newUserDTO) {
        boolean didWork = new UserService().insertUser(newUserDTO);
        String message;
        if(didWork) {
            message = "User erfolgreich angelegt";
            return Response.status(Response.Status.ACCEPTED).entity(message).build();
        }
        message = "Username existiert bereits";
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }

    @GET
    @Path("/login")
    @Produces("text/plain")
    public String login(NewUserDTO newUserDTO) {
        return newUserDTO.getUsername() + " " + newUserDTO.getPassword();
    }

}