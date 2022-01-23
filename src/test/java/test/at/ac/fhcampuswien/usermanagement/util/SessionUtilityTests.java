package test.at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.Session;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;
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
    public void getUser_dontFindInactiveUser(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        Date sessionValidDate = calendar.getTime();

        Date currentDate = Calendar.getInstance().getTime();

        try (MockedStatic<SessionUtility> sessionUtilityMock = Mockito.mockStatic(SessionUtility.class, Mockito.CALLS_REAL_METHODS)){
            sessionUtilityMock.when(() -> SessionUtility.sessionValidUntil())
                    .thenReturn(sessionValidDate);
            sessionUtilityMock.when(() -> SessionUtility.currentDate())
                    .thenReturn(currentDate);

            NewUserDTO newUser = new NewUserDTO();

            UUID sessionID = SessionUtility.createNewSessionForUser(newUser);
            NewUserDTO foundUser = SessionUtility.getUser(sessionID);

            assertEquals(null, foundUser);
        }
    }

    @Test
    public void getUser_dontFindInvalidUser(){
        UUID invalidSessionID = UUID.randomUUID();
        NewUserDTO foundUser = SessionUtility.getUser(invalidSessionID);

        assertEquals(null, foundUser);
    }
}
