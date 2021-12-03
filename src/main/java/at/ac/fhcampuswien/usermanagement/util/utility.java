package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;


public class utility {

    public static String hashPW(String password){
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());

        return hashpw;
    }

    public static Boolean checkPW(String password, String hash){
        Boolean result = BCrypt.checkpw(password, hash);
        return result;
    }
}
