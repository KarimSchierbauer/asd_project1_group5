package at.ac.fhcampuswien.usermanagement.util;

import java.util.ArrayList;
import java.util.List;

public class LoginLockoutService {
    private static ArrayList<String> _badLogins = new ArrayList<String>();

    public static boolean isLockedOut(String username){
        return _badLogins.stream().filter(s -> s.equals(username)).count() >= 3;
    }

    public static void failedLogin(String username){
        _badLogins.add(username);
    }
}
