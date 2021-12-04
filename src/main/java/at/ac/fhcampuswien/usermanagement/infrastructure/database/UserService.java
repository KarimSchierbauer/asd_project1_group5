package at.ac.fhcampuswien.usermanagement.infrastructure.database;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.Utility;

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

        boolean usernameTaken = usernameAlreadyTaken(newUserDTO.getUsername());
        if(usernameTaken){
            return false;
        }

        String rawCommand = "INSERT INTO userSchema.userEntity (firstname,lastname,username,password) VALUES ('%s', '%s', '%s', '%s');";
        String hashpw = Utility.hashPW(newUserDTO.getPassword());

        try {
            Statement stmt = connection.createStatement();
            String sql = String.format(rawCommand, newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getUsername(), hashpw);
            System.out.println(sql);
            stmt.executeUpdate(sql);
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean usernameAlreadyTaken(String username){
        Connection connection = getConnection();

        try {
            PreparedStatement rawCommand = connection.prepareStatement("SELECT username FROM userSchema.userEntity WHERE username = ?");
            rawCommand.setString(1, username);
            ResultSet rs = rawCommand.executeQuery();
            while (rs.next()) {
                String usernameFromDb = rs.getString("username");
                if (usernameFromDb == null || usernameFromDb.isEmpty()) {
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
                String usernameFromDb = rs.getString("username");
                if(!usernameFromDb.equals(newUserDTO.getUsername())){
                    continue;
                }
                String passwordFromDb = rs.getString("password");
                if (Utility.checkPW(newUserDTO.getPassword(), passwordFromDb)) {
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

    public boolean deleteUser(String username){
        Connection connection = getConnection();

        try {
            PreparedStatement rawCommand = connection.prepareStatement("DELETE FROM userSchema.userEntity WHERE username = ?");
            rawCommand.setString(1, username);
            int updatedRows = rawCommand.executeUpdate();
            rawCommand.close();
            connection.close();
            return updatedRows > 0;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePW(NewUserDTO newUserDTO, String password){
        Connection connection = getConnection();
        newUserDTO.setPassword(password);
        String newHashedPW = Utility.hashPW(newUserDTO.getPassword());

        try{
            PreparedStatement rawCommand = connection.prepareStatement("UPDATE userSchema.userEntity SET password = ? WHERE username = ?");
            rawCommand.setString(1, newHashedPW);
            rawCommand.setString(2, newUserDTO.getUsername());
            rawCommand.executeUpdate();
            boolean b = (checkUser(newUserDTO));
            rawCommand.close();
            connection.close();
            return b;
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
