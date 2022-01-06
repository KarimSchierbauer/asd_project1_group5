package at.ac.fhcampuswien.usermanagement;

import at.ac.fhcampuswien.usermanagement.infrastructure.database.UserService;
import at.ac.fhcampuswien.usermanagement.models.ChangePasswordDTO;
import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.LoginLockoutService;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;
import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
public class UserManagement {
    private final String SessionHeader = "SessionId";
    private final String TransactionHeader = "TransactionId";

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
        if (!PasswordUtility.checkPwNotCommon(newUserDTO.getPassword())) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Passwort unsicher! Bitte geben Sie ein anderes Passwort ein.")
                    .build();
        }
        if (!PasswordUtility.PwContainsSpecialChars(newUserDTO.getPassword())) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Passwort muss ein Sonderzeichen enthalten.")
                    .build();
        }

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
        if (LoginLockoutService.isLockedOut(newUserDTO.getUsername())) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Zu viele Fehlversuche")
                    .build();
        }
        if(new UserService().checkUser(newUserDTO)){
            message = "User erfolgreich eingeloggt";
            UUID sessionId = SessionUtility.createNewSessionForUser(newUserDTO);
            return Response.status(Response.Status.OK).entity(message).header(SessionHeader, sessionId).build();
        }
        LoginLockoutService.failedLogin(newUserDTO.getUsername());
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
                    .entity("Header fehlt '" + SessionHeader + "'")
                    .build();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            NewUserDTO user = SessionUtility.getUser(sessionId);
            return Response
                    .status(Response.Status.OK)
                    .entity("Eingeloggt als: '" + user.getUsername() + "'")
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Sitzung ist nicht gültig!")
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
                    .entity("Header fehlt '" + SessionHeader + "'")
                    .build();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            SessionUtility.closeSession(sessionId);
            return Response
                    .status(Response.Status.OK)
                    .entity("Erfolgreich ausgeloggt")
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Ungültige Sitzungs ID")
                    .build();
        }
    }

    @DELETE
    @Path("/account")
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@HeaderParam(SessionHeader) UUID sessionId, @HeaderParam(TransactionHeader) UUID transactionId){
        if (sessionId == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Header fehlt '" + SessionHeader + "'")
                    .build();

        if(transactionId == null){
            return Response
                    .status(Response.Status.OK)
                    .entity("Wollen Sie den Account wirklich löschen? Sind Sie sich den Konsequenzen bewusst? Verstehen Sie was Sie tun?")
                    .header(TransactionHeader, UUID.randomUUID())
                    .build();
        }

        if (SessionUtility.isSessionStillActive(sessionId)) {
            NewUserDTO user = SessionUtility.getUser(sessionId);
            boolean b = new UserService().deleteUser(user.getUsername());
            if (b){
                SessionUtility.closeSession(sessionId);
                return Response
                        .status(Response.Status.OK)
                        .entity("User erfolgreich gelöscht")
                        .build();
            }
            else {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("User konnte nicht gelöscht werden!")
                        .build();
            }
        }
        else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Ungültige Sitzungs ID")
                    .build();
        }
    }

    @PUT
    @Path("/updatePW")
    @Produces("text/plain")
    public Response updatePW(@HeaderParam(SessionHeader) UUID sessionId, ChangePasswordDTO changePasswordDTO) {
        if (sessionId == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Header fehlt '" + SessionHeader + "'")
                    .build();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            NewUserDTO user = SessionUtility.getUser(sessionId);
            if (PasswordUtility.checkStringNotEmpty(changePasswordDTO.getInitialPassword()) || PasswordUtility.checkStringNotEmpty(changePasswordDTO.getRepeatedPassword())) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Eines der Passwörter ist leer")
                        .build();
            }
            if (!PasswordUtility.checkPwNotCommon(changePasswordDTO.getInitialPassword()) || !PasswordUtility.checkPwNotCommon(changePasswordDTO.getRepeatedPassword())) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Passwort unsicher! Bitte geben Sie ein anderes Passwort ein.")
                        .build();
            }
            if (PasswordUtility.checkPwTooLong(changePasswordDTO.getInitialPassword()) || PasswordUtility.checkPwTooLong(changePasswordDTO.getRepeatedPassword())) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Passwort zu lang! Bitte geben Sie ein anderes Passwort ein.")
                        .build();
            }

            if (!PasswordUtility.PwContainsSpecialChars(changePasswordDTO.getInitialPassword()) || !PasswordUtility.PwContainsSpecialChars(changePasswordDTO.getRepeatedPassword())) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Passwort muss ein Sonderzeichen enthalten.")
                        .build();
            }
            if (!PasswordUtility.checkIdenticalPW(changePasswordDTO.getInitialPassword(), changePasswordDTO.getRepeatedPassword())) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Kennwörter nicht gleich ausgeben")
                        .build();
            } else {
                boolean didWork = new UserService().changePW(user, changePasswordDTO.getInitialPassword());
                if(didWork){
                    return Response
                            .status(Response.Status.OK)
                            .entity("Passwort wurde erfolgreich geändert")
                            .build();
                }
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Ein Fehler ist aufgetreten")
                        .build();
            }
        }else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Ungültige Sitzungs ID")
                    .build();
        }
    }
}