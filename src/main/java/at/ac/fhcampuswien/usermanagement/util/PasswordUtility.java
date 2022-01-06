package at.ac.fhcampuswien.usermanagement.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordUtility {

    private PasswordUtility(){

    }

    private static final String SPECIAL_CHARACTER_REGEX = ".*[!@#$%*()'+,-./:;<=>?\\[\\]^_`{|}]+.*";
    private static final int PASSWORD_MAX_LENGTH = 255;
    private static final int PASSWORD_MIN_LENGTH = 3;
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
            try (BufferedReader commonPwRead = new BufferedReader(commonPw)) {

                String commonPwLine = commonPwRead.readLine();
                while (commonPwLine != null) {
                    passwords.add(commonPwLine);
                    commonPwLine = commonPwRead.readLine();
                }
            }
            passwordList = passwords;
        }

        return passwordList;
    }

    public static boolean checkPwNotCommon(String password){
        if (password.length() <= PASSWORD_MIN_LENGTH) {
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
        return password.length() > PASSWORD_MAX_LENGTH;
    }

    public static boolean pwContainsSpecialChars(String password){
        return password.matches(SPECIAL_CHARACTER_REGEX);
    }

    public static boolean checkStringNotEmpty(String text){
        return (text == null || text.isEmpty());
    }

    public static String checkValidity(String passwordToCheckValidity) {
        if (!checkPwNotCommon(passwordToCheckValidity))
            return "Passwort unsicher! Bitte geben Sie ein anderes Passwort ein.";

        if (pwContainsSpecialChars(passwordToCheckValidity))
            return "Passwort muss ein Sonderzeichen enthalten.";

        return null;
    }

    public static String checkValidity(String initialPassword, String repeatedPassword) {
        if (checkStringNotEmpty(initialPassword) || checkStringNotEmpty(repeatedPassword))
            return "Eines der Passwörter ist leer";

        if (!checkIdenticalPW(initialPassword, repeatedPassword))
            return "Kennwörter nicht gleich ausgeben";

        if (!checkPwNotCommon(initialPassword))
            return "Passwort unsicher! Bitte geben Sie ein anderes Passwort ein.";

        if (checkPwTooLong(initialPassword))
            return "Passwort zu lang! Bitte geben Sie ein anderes Passwort ein.";

        if (!pwContainsSpecialChars(initialPassword))
            return "Passwort muss ein Sonderzeichen enthalten.";

        return null;
    }
}
