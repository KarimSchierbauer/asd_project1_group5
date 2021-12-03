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
            String rawCommand = "INSERT INTO userSchema.userEntity (firstname,lastname,username,password) VALUES ('%s', '%s', '%s', '%s' );";

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
            System.out.println("Username is already taken. Try again.");
            return false;
        }

        return true;
    }

    public boolean compareUsername(String username){
        Connection connection = getConnection();
        boolean nameTaken;

        try {
            PreparedStatement rawCommand = connection.prepareStatement("SELECT username FROM userSchema.userEntity WHERE username=?");
            rawCommand.setString(1, username);
            ResultSet rs = rawCommand.executeQuery();
            while (rs.next()) {
                if (rs.getString("username") == null || rs.getString("username").isEmpty()) {
                    //TODO: replace System.out.println with exception \n
                    // --> error message must be shown in window
                    System.out.println("Username is not taken");
                    return nameTaken = false;
                } else {
                    System.out.println("Username " + username + " is already taken. Try again.");
                    return nameTaken = true;
                }
            }
            rawCommand.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public void compareUser(NewUserDTO newUserDTO){
//        //Connection connection = getConnection();
//
//        String hashpw = utility.hashPW(newUserDTO.getPassword());
//
//        try {
//            PreparedStatement rawCommand = connection.prepareStatement("SELECT username, password FROM userSchema.userEntity WHERE username=? AND password=?");
//            rawCommand.setString(1, newUserDTO.getUsername());
//            rawCommand.setString(2, hashpw);
//            ResultSet rs = rawCommand.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

}
