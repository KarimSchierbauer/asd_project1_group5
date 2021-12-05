package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;
import java.io.FileReader;
import java.io.BufferedReader;

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
        if(password1.equals(password2)){
            return true;
        }
        return false;
    }

    public static boolean checkPWnotCommon(String password) throws IOException {
        //Check if password is empty
        if (password.length() <= 3 || password.length() > 255) {
            return false;
        }
        try (FileReader commonPW = new FileReader("/resources/10kcommonPW.txt") {
            BufferedReader commonPWread = new BufferedReader(commonPW));
            String commonPWline = commonPWread.readLine();
            //Check if password exists in txt-file
            while (commonPWline != null && !commonPWline.isEmpty()) {
                if (commonPWline.contains(password)) {
                    return false;
                }
                commonPWline = commonPWread.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean checkStringNotEmpty(String text){
        boolean result = ((text == null || text.isEmpty()) ? true : false);
        return result;
    }
}
