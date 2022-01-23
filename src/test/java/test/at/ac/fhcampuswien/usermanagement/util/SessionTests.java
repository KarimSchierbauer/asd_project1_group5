package test.at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.Session;
import at.ac.fhcampuswien.usermanagement.util.SessionUtility;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTests {

    @Test
    public void newUserDTO_isSaved(){
        NewUserDTO newUserDTO = new NewUserDTO();

        Session newSession = new Session(newUserDTO);

        assertEquals(newUserDTO, newSession.getNewUserDTO());
    }

    @Test
    public void session_invalid(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        Date sessionValidDate = calendar.getTime();

        Date currentDate = Calendar.getInstance().getTime();

        try (MockedStatic<SessionUtility> sessionUtilityMock = Mockito.mockStatic(SessionUtility.class)){
            sessionUtilityMock.when(() -> SessionUtility.sessionValidUntil())
                    .thenReturn(sessionValidDate);
            sessionUtilityMock.when(() -> SessionUtility.currentDate())
                    .thenReturn(currentDate);

            NewUserDTO newUserDTO = new NewUserDTO();

            Session newSession = new Session(newUserDTO);

            assertFalse(newSession.getIsStillValid());
        }
    }

    @Test
    public void session_valid(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        Date sessionValidDate = calendar.getTime();

        Date currentDate = Calendar.getInstance().getTime();

        try (MockedStatic<SessionUtility> sessionUtilityMock = Mockito.mockStatic(SessionUtility.class)){
            sessionUtilityMock.when(() -> SessionUtility.sessionValidUntil())
                    .thenReturn(sessionValidDate);
            sessionUtilityMock.when(() -> SessionUtility.currentDate())
                    .thenReturn(currentDate);

            NewUserDTO newUserDTO = new NewUserDTO();

            Session newSession = new Session(newUserDTO);

            assertTrue(newSession.getIsStillValid());
        }
    }
}
