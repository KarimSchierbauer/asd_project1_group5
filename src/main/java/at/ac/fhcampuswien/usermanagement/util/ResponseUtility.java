package at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.UserManagement;

import javax.ws.rs.core.Response;
import java.util.UUID;

public class ResponseUtility {

    public static Response badRequest(String errorMessage) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }

    private static Response okResponse(String message, UUID sessionId) {
        return Response
                .status(Response.Status.OK)
                .entity(message)
                .header(UserManagement.SessionHeader, sessionId)
                .build();
    }

    private static Response okResponse(String message) {
        return Response
                .status(Response.Status.OK)
                .entity(message)
                .build();
    }

    public static Response missingSession() {
        return badRequest("missing header '" + UserManagement.SessionHeader + "'");
    }

    public static Response userSuccessfullyInserted(UUID newSessionId) {
        return okResponse("User erfolgreich angelegt", newSessionId);
    }

    public static Response userLoggedIn(UUID newSessionId) {
        return okResponse("User eingeloggt", newSessionId);
    }

    public static Response conflictAtUserInsertion() {
        String message = "Username existiert bereits";
        return Response
                .status(Response.Status.CONFLICT)
                .entity(message)
                .build();
    }

    public static Response tooManyRequests() {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity("Zu viele Fehlversuche")
                .build();
    }

    public static Response invalidCredentials() {
        String message = "Username oder Passwort nicht korrekt";
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .build();
    }

    public static Response loggedInAs(String username) {
        return okResponse("logged in as: '" + username + "'");
    }

    public static Response invalidSession() {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Session is not valid")
                .build();
    }

    public static Response badSessionId() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Bad SessionId")
                .build();
    }

    public static Response loggedOut() {
        return okResponse("Successfully logged out");
    }

    public static Response wantToDeleteAccount() {
        return Response
                .status(Response.Status.OK)
                .entity("Wollen Sie den Account wirklich löschen? Sind Sie sich den Konsequenzen bewusst? Verstehen Sie was Sie tun?")
                .header(UserManagement.TransactionHeader, UUID.randomUUID())
                .build();
    }

    public static Response couldNotDeleteUser() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Couldn't delete user")
                .build();
    }

    public static Response userSuccessfullyDeleted() {
        return okResponse("Successfully deleted User");
    }

    public static Response error() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Ein Fehler ist aufgetreten")
                .build();
    }

    public static Response passwordSuccessfullyChanged() {
        return Response
                .status(Response.Status.OK)
                .entity("Passwort wurde erfolgreich geändert")
                .build();
    }
}
