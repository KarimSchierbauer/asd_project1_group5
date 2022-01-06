package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordUtility {

    private static List<String> passwordList;

    public static String hashPW(String password){

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPW(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

    public static boolean checkIdenticalPW(String initialPassword, String repeatedPassword){
        return initialPassword.equals(repeatedPassword);
    }

    private static List<String> getPasswords() throws IOException {
        if(passwordList == null){
            ArrayList<String> passwords = new ArrayList<>();

            String file = PasswordUtility.class.getResource("/10kcommonPW.txt").getFile();
            FileReader commonPw = new FileReader(file);
            BufferedReader commonPwRead = new BufferedReader(commonPw);

            String commonPwLine = commonPwRead.readLine();
            while (commonPwLine != null){
                passwords.add(commonPwLine);
                commonPwLine = commonPwRead.readLine();
            }
            passwordList = passwords;
        }

        return passwordList;
    }

    public static boolean checkPwNotCommon(String password){
        //Check if password is longer than 3 characters
        if (password.length() <= 3) {
            return false;
        }

        try {
            List<String> passwords = getPasswords();
            return !passwords.contains(password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPwTooLong(String password){
        return password.length() > 255;
    }

    public static boolean PwContainsSpecialChars(String password){
        String specialCharacterRegex = ".*[!@#$%*()'+,-./:;<=>?\\[\\]^_`{|}]+.*";
        return password.matches(specialCharacterRegex);
    }

    public static boolean checkStringNotEmpty(String text){
        return (text == null || text.isEmpty());
    }
}
