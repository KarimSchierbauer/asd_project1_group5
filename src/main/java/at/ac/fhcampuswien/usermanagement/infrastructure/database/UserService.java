package at.ac.fhcampuswien.usermanagement.infrastructure.database;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserService {
    private Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_usermanager",
                    "username", "password");
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public void insertUser(NewUserDTO newUserDTO){
        Connection connection = getConnection();
        String rawCommand = "INSERT INTO userSchema.userEntity (firstname,lastname,username,password) VALUES ('%s', '%s', '%s', '%s' );";

        String hashpw = BCrypt.hashpw(newUserDTO.getPassword(), BCrypt.gensalt());

        //if (BCrypt.checkpw(newUserDTO.getPassword(), hashpw)) {
        //    System.out.println("Password is fine");
        //}

        try {
            Statement stmt = connection.createStatement();
            String sql = String.format(rawCommand, newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getUsername(), hashpw);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
