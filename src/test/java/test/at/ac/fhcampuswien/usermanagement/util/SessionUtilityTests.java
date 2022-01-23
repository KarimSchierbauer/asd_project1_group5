package test.at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionUtilityTests {

    @Test
    public void newSession_IsStillActive(){
        NewUserDTO newUser = new NewUserDTO();

        UUID sessionID = SessionUtility.createNewSessionForUser(newUser);
        boolean sessionStillActive = SessionUtility.isSessionStillActive(sessionID);

        assertTrue(sessionStillActive);


        SessionUtility.closeSession(sessionID);

        sessionStillActive = SessionUtility.isSessionStillActive(sessionID);

        assertFalse(sessionStillActive);
    }

    @Test
    public void invalidSession_IsNotActive(){
        UUID invalidSessionID = UUID.randomUUID();
        boolean sessionStillActive = SessionUtility.isSessionStillActive(invalidSessionID);

        assertFalse(sessionStillActive);
    }

    @Test
    public void getUser_findsValidUser(){
        NewUserDTO newUser = new NewUserDTO();

        UUID sessionID = SessionUtility.createNewSessionForUser(newUser);
        NewUserDTO foundUser = SessionUtility.getUser(sessionID);

        assertEquals(newUser, foundUser);
    }

    @Test
    public void getUser_dontFindInvalidUser(){
        UUID invalidSessionID = UUID.randomUUID();
        NewUserDTO foundUser = SessionUtility.getUser(invalidSessionID);

        assertEquals(null, foundUser);
    }
}
