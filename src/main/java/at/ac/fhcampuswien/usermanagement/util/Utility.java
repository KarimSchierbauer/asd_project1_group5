package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;


public class Utility {

    public static String hashPW(String password){
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());

        return hashpw;
    }

    public static boolean checkPW(String password, String hash){
        boolean result = BCrypt.checkpw(password, hash);
        return result;
    }

    public static boolean checkIdenticalPW(String password1, String password2){
        boolean result = false;
        if(password1.equals(password2)){
            result = true;
        }
        return result;
    }

    public static boolean checkStringNotEmpty(String text){
        boolean result = ((text == null || text.isEmpty()) ? true : false);
        return result;
    }
}
