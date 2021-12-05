package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    private static List<String> _passwords;

    public static String hashPW(String password){
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());

        return hashpw;
    }

    public static boolean checkPW(String password, String hash){
        boolean result = BCrypt.checkpw(password, hash);
        return result;
    }

    public static boolean checkIdenticalPW(String password1, String password2){
        return password1.equals(password2);
    }

    private static List<String> getPasswords() throws IOException {
        if(_passwords == null){
            ArrayList<String> passwords = new ArrayList<>();

            String file = Utility.class.getResource("/10kcommonPW.txt").getFile();
            FileReader commonPW = new FileReader(file);
            BufferedReader commonPWread = new BufferedReader(commonPW);

            String commonPWline = commonPWread.readLine();
            while (commonPWline != null){
                passwords.add(commonPWline);
                commonPWline = commonPWread.readLine();
            }
            _passwords = passwords;
        }

        return _passwords;
    }

    public static boolean checkPWnotCommon(String password){
        //Check if password is longer than 3 characters
        if (password.length() <= 3) {
            return false;
        }

        try {
            List<String> passwords = getPasswords();
            return !passwords.contains(password);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPWtoolong(String password){
        return password.length() > 255;
    }

    public static boolean PWcontainsspecialchars(String password){
        String specialCharacterRegex = ".*[!@#$%*()'+,-./:;<=>?\\[\\]^_`{|}]+.*";
        return password.matches(specialCharacterRegex);
    }

    public static boolean checkStringNotEmpty(String text){
        boolean result = ((text == null || text.isEmpty()) ? true : false);
        return result;
    }
}
