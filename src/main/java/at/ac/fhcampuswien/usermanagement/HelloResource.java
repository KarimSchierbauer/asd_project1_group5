package at.ac.fhcampuswien.usermanagement;

import javax.ws.rs.*;

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

}