package at.ac.fhcampuswien.usermanagement;

import at.ac.fhcampuswien.usermanagement.infrastructure.database.UserService;
import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.Session;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/hello-world")
public class HelloResource {
    private final String SessionHeader = "SessionId";

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
            UUID sessionId = SessionUtility.createNewSessionForUser(newUserDTO);
            return Response.status(Response.Status.OK).entity(message).header(SessionHeader, sessionId).build();
        }
        message = "Username existiert bereits";
        return Response.status(Response.Status.CONFLICT).entity(message).build();
    }

    @GET
    @Path("/login")
    @Produces("text/plain")
    public Response login(NewUserDTO newUserDTO) {
        String message;
        if(new UserService().checkUser(newUserDTO)){
            message = "User erfolgreich eingeloggt";
            UUID sessionId = SessionUtility.createNewSessionForUser(newUserDTO);
            return Response.status(Response.Status.OK).entity(message).header(SessionHeader, sessionId).build();
        }
        message = "Username oder Passwort nicht korrekt";
        return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
    }

    @GET
    @Path("/isSessionActive")
    @Produces("text/plain")
    public Response isSessionActive(@HeaderParam(SessionHeader) UUID sessionId){
        if (sessionId == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("missing header '" + SessionHeader + "'")
                    .build();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            NewUserDTO user = SessionUtility.getUser(sessionId);
            return Response
                    .status(Response.Status.OK)
                    .entity("logged in as: '" + user.getUsername() + "'")
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Session is not valid")
                    .build();
        }
    }

    @POST
    @Path("/logout")
    @Produces("text/plain")
    public Response logout(@HeaderParam(SessionHeader) UUID sessionId){
        if (sessionId == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("missing header '" + SessionHeader + "'")
                    .build();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            SessionUtility.closeSession(sessionId);
            return Response
                    .status(Response.Status.OK)
                    .entity("Successfully logged out")
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Bad SessionId")
                    .build();
        }
    }

}