package at.ac.fhcampuswien.usermanagement.infrastructure.database;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.utility;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

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

    public boolean insertUser(NewUserDTO newUserDTO){
        Connection connection = getConnection();
        boolean usernameTaken= compareUsername(newUserDTO.getUsername());
        if(!usernameTaken){
            String rawCommand = "INSERT INTO userSchema.userEntity (firstname,lastname,username,password) VALUES ('%s', '%s', '%s', '%s');";
            String hashpw = utility.hashPW(newUserDTO.getPassword());

            //if (BCrypt.checkpw(newUserDTO.getPassword(), hashpw)) {
            //    System.out.println("Password is fine");
            //}

            try {
                Statement stmt = connection.createStatement();
                String sql = String.format(rawCommand, newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getUsername(), hashpw);
                System.out.println(sql);
                stmt.executeUpdate(sql);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            return false;
        }

        return true;
    }

    public boolean compareUsername(String username){
        Connection connection = getConnection();

        try {
            PreparedStatement rawCommand = connection.prepareStatement("SELECT username FROM userSchema.userEntity WHERE username=?");
            rawCommand.setString(1, username);
            ResultSet rs = rawCommand.executeQuery();
            while (rs.next()) {
                if (rs.getString("username") == null || rs.getString("username").isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
            rawCommand.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(NewUserDTO newUserDTO) {
        Connection connection = getConnection();

        try {
            PreparedStatement rawCommand = connection.prepareStatement("SELECT username, password FROM userSchema.userEntity WHERE username=?");
            rawCommand.setString(1, newUserDTO.getUsername());
            ResultSet rs = rawCommand.executeQuery();
            while (rs.next()) {
                if (rs.getString("username") == newUserDTO.getUsername() && utility.checkPW(newUserDTO.getPassword(), rs.getString("password"))) {
                    return true;
                } else {
                    return false;
                }
            }
            rawCommand.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
