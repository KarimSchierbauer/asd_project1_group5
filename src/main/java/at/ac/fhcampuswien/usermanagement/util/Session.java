package at.ac.fhcampuswien.usermanagement.util;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;

import java.util.Date;

public class Session {
    private NewUserDTO newUserDTO;
    private Date validUntil;

    public Session(NewUserDTO newUserDTO){
        this.newUserDTO = newUserDTO;
        this.validUntil = SessionUtility.sessionValidUntil();
    }


    public NewUserDTO getNewUserDTO() {
        return newUserDTO;
    }

    public boolean getIsStillValid(){
        if (validUntil.compareTo(SessionUtility.currentDate()) < 0) {
            return false;
        }

        updateValidUntil();
        return true;
    }

    private void updateValidUntil(){
        validUntil = SessionUtility.sessionValidUntil();
    }
}
