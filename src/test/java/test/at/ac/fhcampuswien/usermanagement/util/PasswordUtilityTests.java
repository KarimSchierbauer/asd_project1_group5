package test.at.ac.fhcampuswien.usermanagement.util;

import static org.junit.jupiter.api.Assertions.*;

import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class PasswordUtilityTests {

    @Test
    public void PasswordUtility_hashPW_and_checkPW(){
        String passwordToTest = "TestPAssw0Rd";

        String passwordHash = PasswordUtility.hashPW(passwordToTest);
        boolean passwordFitsPasswordHash = PasswordUtility.checkPW(passwordToTest, passwordHash);

        assertTrue(passwordFitsPasswordHash);
    }



    private static Stream<Arguments> testParametersFor_PasswordUtility_checkIdenticalPW_samePassword() {
        return Stream.of(
                Arguments.of("TestPAssw0Rd", "TestPAssw0Rd", true),
                Arguments.of("TestPAssw0Rd", "aoeuaoeuaoeu", false),
                Arguments.of("", "aoeuaoeuaoeu", false)
        );
    }

    @ParameterizedTest
    @MethodSource("testParametersFor_PasswordUtility_checkIdenticalPW_samePassword")
    public void PasswordUtility_checkIdenticalPW_samePassword(String passwordToCompare1, String passwordToCompare2, boolean areEqualExpected){
        boolean passwordsAreIdentical = PasswordUtility.checkIdenticalPW(passwordToCompare1, passwordToCompare2);

        assertEquals(areEqualExpected, passwordsAreIdentical);;
    }

    private static Stream<Arguments> testParametersFor_PasswordUtility_checkValidity_1() {
        return Stream.of(
                Arguments.of("TestPAssw0Rd+", null),
                Arguments.of("password", "Passwort unsicher! Bitte geben Sie ein anderes Passwort ein."),
                Arguments.of("aoeu", "Passwort muss ein Sonderzeichen enthalten.")
        );
    }

    @ParameterizedTest
    @MethodSource("testParametersFor_PasswordUtility_checkValidity_1")
    public void PasswordUtility_checkValidity_1(String passwordToCheck, String expectedValidityOutput){
        String validityOutput = PasswordUtility.checkValidity(passwordToCheck);

        assertEquals(expectedValidityOutput, validityOutput);
    }

    private static Stream<Arguments> testParametersFor_PasswordUtility_checkValidity_2() {
        return Stream.of(
                Arguments.of("TestPAssw0Rd+", null, "Eines der Passwörter ist leer"),
                Arguments.of("TestPAssw0Rd+", "", "Eines der Passwörter ist leer"),
                Arguments.of(null, "TestPAssw0Rd+", "Eines der Passwörter ist leer"),
                Arguments.of("TestPAssw0Rd+", "aoeu+", "Kennwörter nicht gleich ausgeben"),
                Arguments.of("password", "password", "Passwort unsicher! Bitte geben Sie ein anderes Passwort ein."),
                Arguments.of("ad", "ad", "Passwort unsicher! Bitte geben Sie ein anderes Passwort ein."),
                Arguments.of("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "Passwort zu lang! Bitte geben Sie ein anderes Passwort ein."),
                Arguments.of("TestPAssw0Rd", "TestPAssw0Rd", "Passwort muss ein Sonderzeichen enthalten."),
                Arguments.of("TestPAssw0Rd+", "TestPAssw0Rd+", null)
        );
    }

    @ParameterizedTest
    @MethodSource("testParametersFor_PasswordUtility_checkValidity_2")
    public void PasswordUtility_checkValidity_2(String passwordToCheck1, String passwordToCheck2, String expectedValidityOutput){
        String validityOutput = PasswordUtility.checkValidity(passwordToCheck1, passwordToCheck2);

        assertEquals(expectedValidityOutput, validityOutput);
    }
}
