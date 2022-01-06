package at.ac.fhcampuswien.usermanagement;

import at.ac.fhcampuswien.usermanagement.infrastructure.database.UserService;
import at.ac.fhcampuswien.usermanagement.models.ChangePasswordDTO;
import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.LoginLockoutService;
import at.ac.fhcampuswien.usermanagement.util.ResponseUtility;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;
import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
public class UserManagement {
    public static final String SessionHeader = "SessionId";
    public static final String TransactionHeader = "TransactionId";

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public Response register(NewUserDTO newUserDTO) {

        String validationErrorMessage = PasswordUtility.checkValidity(newUserDTO.getPassword());
        if (validationErrorMessage != null)
            return ResponseUtility.badRequest(validationErrorMessage);

        boolean userInsertionWasSuccessful = new UserService().insertUser(newUserDTO);

        if (!userInsertionWasSuccessful)
            return ResponseUtility.conflictAtUserInsertion();

        UUID sessionId = SessionUtility.createNewSessionForUser(newUserDTO);
        return ResponseUtility.userSuccessfullyInserted(sessionId);

    }

    @GET
    @Path("/login")
    @Produces("text/plain")
    public Response login(NewUserDTO newUserDTO) {
        if (LoginLockoutService.isLockedOut(newUserDTO.getUsername()))
            return ResponseUtility.tooManyRequests();

        if (new UserService().checkUser(newUserDTO)) {
            UUID sessionId = SessionUtility.createNewSessionForUser(newUserDTO);
            return ResponseUtility.userLoggedIn(sessionId);
        }

        LoginLockoutService.failedLogin(newUserDTO.getUsername());
        return ResponseUtility.invalidCredentials();
    }

    @GET
    @Path("/isSessionActive")
    @Produces("text/plain")
    public Response isSessionActive(@HeaderParam(SessionHeader) UUID sessionId) {
        if (sessionId == null)
            return ResponseUtility.missingSession();

        if (!SessionUtility.isSessionStillActive(sessionId))
            return ResponseUtility.invalidSession();

        NewUserDTO user = SessionUtility.getUser(sessionId);
        return ResponseUtility.loggedInAs(user.getUsername());
    }

    @POST
    @Path("/logout")
    @Produces("text/plain")
    public Response logout(@HeaderParam(SessionHeader) UUID sessionId) {
        if (sessionId == null)
            return ResponseUtility.missingSession();

        if (SessionUtility.isSessionStillActive(sessionId)) {
            SessionUtility.closeSession(sessionId);
            return ResponseUtility.loggedOut();
        }

        return ResponseUtility.badSessionId();
    }

    @DELETE
    @Path("/account")
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@HeaderParam(SessionHeader) UUID sessionId, @HeaderParam(TransactionHeader) UUID transactionId) {
        if (sessionId == null)
            return ResponseUtility.missingSession();

        if (transactionId == null)
            return ResponseUtility.wantToDeleteAccount();

        if (!SessionUtility.isSessionStillActive(sessionId))
            return ResponseUtility.badSessionId();

        NewUserDTO user = SessionUtility.getUser(sessionId);
        boolean deletionWasSuccessful = new UserService().deleteUser(user.getUsername());

        if (!deletionWasSuccessful)
            return ResponseUtility.couldNotDeleteUser();

        SessionUtility.closeSession(sessionId);
        return ResponseUtility.userSuccessfullyDeleted();

    }

    @PUT
    @Path("/updatePW")
    @Produces("text/plain")
    public Response updatePW(@HeaderParam(SessionHeader) UUID sessionId, ChangePasswordDTO changePasswordDTO) {
        if (sessionId == null)
            return ResponseUtility.missingSession();

        if (!SessionUtility.isSessionStillActive(sessionId))
            return ResponseUtility.badSessionId();

        NewUserDTO user = SessionUtility.getUser(sessionId);

        String validationErrorMessage = PasswordUtility.checkValidity(changePasswordDTO.getInitialPassword(), changePasswordDTO.getRepeatedPassword());
        if (validationErrorMessage != null)
            return ResponseUtility.badRequest(validationErrorMessage);

        boolean passwordChangeWorked = new UserService().changePW(user, changePasswordDTO.getInitialPassword());

        if (!passwordChangeWorked)
            return ResponseUtility.error();

        return ResponseUtility.passwordSuccessfullyChanged();
    }
}
