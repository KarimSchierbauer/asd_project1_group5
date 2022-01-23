package test.at.ac.fhcampuswien.usermanagement.util;

import static org.junit.Assert.*;

import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;
import org.junit.Test;
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
}
