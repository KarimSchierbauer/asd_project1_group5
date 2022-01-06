package at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;

import java.util.*;

public class SessionUtility {

    private static final int ONE = 1;

    private static Hashtable<UUID, Session> sessionsTable = new Hashtable();

    public static UUID createNewSessionForUser(NewUserDTO newUserDTO){
        Session session = new Session(newUserDTO);

        UUID thisSessionId = UUID.randomUUID();
        sessionsTable.put(thisSessionId, session);
        return thisSessionId;
    }

    public static boolean isSessionStillActive(UUID sessionId){
        if (sessionsTable.containsKey(sessionId)) {
            Session session = sessionsTable.get(sessionId);
            return session.getIsStillValid();
        }
        return false;
    }

    public static NewUserDTO getUser(UUID sessionId){
        if (sessionsTable.containsKey(sessionId)) {
            Session session = sessionsTable.get(sessionId);
            if (session.getIsStillValid())
                return session.getNewUserDTO();
        }
        return null;
    }

    public static void closeSession(UUID sessionId){
        sessionsTable.remove(sessionId);
    }

    public static Date currentDate(){
        Calendar currentDate = Calendar.getInstance();
        return currentDate.getTime();
    }

    public static Date sessionValidUntil(){
        Calendar sessionValidUntil = Calendar.getInstance();
        sessionValidUntil.add(Calendar.MINUTE, ONE);
        return sessionValidUntil.getTime();
    }
}
