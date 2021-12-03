package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;

public class utility {

    public static String hashPW(String password){
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());

        return hashpw;
    }
}
