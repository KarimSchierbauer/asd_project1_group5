package at.ac.fhcampuswien.usermanagement.util;

import java.util.ArrayList;

public class LoginLockoutService {

    private static final int loginCount = 3;

    private static ArrayList<String> badLogins = new ArrayList<String>();

    public static boolean isLockedOut(String username){
        return badLogins.stream().filter(s -> s.equals(username)).count() >= loginCount;
    }

    public static void failedLogin(String username){
        badLogins.add(username);
    }
}
