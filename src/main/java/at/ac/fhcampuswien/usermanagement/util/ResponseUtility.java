package at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.UserManagement;

import javax.ws.rs.core.Response;
import java.util.UUID;

public class ResponseUtility {

    private ResponseUtility(){

    }

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
                .header(UserManagement.SESSION_HEADER, sessionId)
                .build();
    }

    private static Response okResponse(String message) {
        return Response
                .status(Response.Status.OK)
                .entity(message)
                .build();
    }

    public static Response missingSession() {
        return badRequest("Header fehlt '" + UserManagement.SESSION_HEADER + "'");
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
        return okResponse("Eingeloggt als: '" + username + "'");
    }

    public static Response invalidSession() {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Sitzung ist nicht gültig")
                .build();
    }

    public static Response badSessionId() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Ungültige Sitzungs ID")
                .build();
    }

    public static Response loggedOut() {
        return okResponse("Erfolgreich ausgeloggt");
    }

    public static Response wantToDeleteAccount() {
        return Response
                .status(Response.Status.OK)
                .entity("Wollen Sie den Account wirklich löschen? Sind Sie sich den Konsequenzen bewusst? Verstehen Sie was Sie tun?")
                .header(UserManagement.TRANSACTION_HEADER, UUID.randomUUID())
                .build();
    }

    public static Response couldNotDeleteUser() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("User konnte nicht gelöscht werden")
                .build();
    }

    public static Response userSuccessfullyDeleted() {
        return okResponse("User erfolgreich gelöscht");
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

    public static Response userNotFound() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Der User wurde nicht gefunden")
                .build();
    }
}
