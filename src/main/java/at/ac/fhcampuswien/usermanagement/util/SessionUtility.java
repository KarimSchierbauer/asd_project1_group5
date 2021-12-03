package at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;

import java.util.*;

public class SessionUtility {
    private static Hashtable<UUID, Session> _sessions = new Hashtable();

    public static UUID createNewSessionForUser(NewUserDTO newUserDTO){
        Session session = new Session(newUserDTO);

        UUID thisSessionId = UUID.randomUUID();
        _sessions.put(thisSessionId, session);
        return thisSessionId;
    }

    public static boolean isSessionStillActive(UUID sessionId){
        if (_sessions.containsKey(sessionId)) {
            Session session = _sessions.get(sessionId);
            if (session.getIsStillValid())
                return true;
        }
        return false;
    }

    public static NewUserDTO getUser(UUID sessionId){
        if (_sessions.containsKey(sessionId)) {
            Session session = _sessions.get(sessionId);
            if (session.getIsStillValid())
                return session.getNewUserDTO();
        }
        return null;
    }

    public static void closeSession(UUID sessionId){
        if(_sessions.containsKey(sessionId))
            _sessions.remove(sessionId);
    }

    public static Date currentDate(){
        Calendar currentDate = Calendar.getInstance();
        return currentDate.getTime();
    }

    public static Date sessionValidUntil(){
        Calendar sessionValidUntil = Calendar.getInstance();
        sessionValidUntil.add(Calendar.MINUTE, 1);
        return sessionValidUntil.getTime();
    }
}
