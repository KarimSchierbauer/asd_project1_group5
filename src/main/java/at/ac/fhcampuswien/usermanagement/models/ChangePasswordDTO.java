package at.ac.fhcampuswien.usermanagement.models;

public class ChangePasswordDTO {
    private String initialPassword;
    private String repeatedPassword;

    public ChangePasswordDTO() {
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}
