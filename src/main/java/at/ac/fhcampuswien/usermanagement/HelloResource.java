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
    public Response.Status register(NewUserDTO newUserDTO) {
        boolean didWork = new UserService().insertUser(newUserDTO);
        Response.Status responseStatus = null;
        if(didWork) {
            responseStatus = Response.Status.ACCEPTED;
            return responseStatus;
        }
        responseStatus = Response.Status.BAD_REQUEST;
        return responseStatus;
    }

}