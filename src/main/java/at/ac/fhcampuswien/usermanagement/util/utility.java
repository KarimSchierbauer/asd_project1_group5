package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;

public class utility {

    public static String hashPW(String password, String salt){
        String hashpw = BCrypt.hashpw(password, salt);

        return hashpw;
    }

    public static String saltGenerator(){
        return BCrypt.gensalt();
    }

    public static Boolean checkPW(String password, String hash){
        Boolean result = BCrypt.checkpw(password, hash);
        return result;
    }
}
