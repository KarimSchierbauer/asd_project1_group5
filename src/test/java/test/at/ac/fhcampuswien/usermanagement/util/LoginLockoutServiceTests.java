package test.at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.util.LoginLockoutService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginLockoutServiceTests {

    private static Stream<Arguments> testParametersFor_isLockedOut() {
        return Stream.of(
                Arguments.of(0, false),
                Arguments.of(1, false),
                Arguments.of(2, false),
                Arguments.of(3, true),
                Arguments.of(4, true)
        );
    }

    @ParameterizedTest
    @MethodSource("testParametersFor_isLockedOut")
    public void isLockedOut(int numberOfFailedLogins, boolean shouldBeLockedOut){
        String usernameToTest = "username" + numberOfFailedLogins;

        for (int i = 0; i < numberOfFailedLogins; i++) {
            LoginLockoutService.failedLogin(usernameToTest);
        }

        boolean isLockedOut = LoginLockoutService.isLockedOut(usernameToTest);

        assertEquals(shouldBeLockedOut, isLockedOut);
    }
}
