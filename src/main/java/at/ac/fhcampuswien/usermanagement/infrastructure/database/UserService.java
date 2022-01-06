package at.ac.fhcampuswien.usermanagement.infrastructure.database;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;

import java.sql.*;

public class UserService {

    private static final String connectionBaseUrl = "jdbc:postgresql://localhost:5432/db_usermanager";
    private static final String connectionClass = "org.postgresql.Driver";

    private Connection getConnection(){
        try {
            Class.forName(connectionClass);
            return DriverManager.getConnection(connectionBaseUrl,
                    "username", "password");
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

        String sqlQuery = "INSERT INTO userSchema.userEntity (firstname,lastname,username,password) VALUES ('%s', '%s', '%s', '%s');";
        String hashPw = PasswordUtility.hashPW(newUserDTO.getPassword());

        try {
            Statement connectionStatement = connection.createStatement();
            String sqlStatement = String.format(sqlQuery, newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getUsername(), hashPw);
            System.out.println(sqlStatement);
            connectionStatement.executeUpdate(sqlStatement);
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
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT username FROM userSchema.userEntity WHERE username = ?");
            sqlQuery.setString(1, username);
            ResultSet rs = sqlQuery.executeQuery();
            while (rs.next()) {
                String usernameFromDb = rs.getString("username");
                if (usernameFromDb == null || usernameFromDb.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
            sqlQuery.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(NewUserDTO newUserDTO) {
        Connection connection = getConnection();

        try {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT username, password FROM userSchema.userEntity WHERE username=?");
            sqlQuery.setString(1, newUserDTO.getUsername());
            ResultSet rs = sqlQuery.executeQuery();
            while (rs.next()) {
                String usernameFromDb = rs.getString("username");
                if(!usernameFromDb.equals(newUserDTO.getUsername())){
                    continue;
                }
                String passwordFromDb = rs.getString("password");
                if (PasswordUtility.checkPW(newUserDTO.getPassword(), passwordFromDb)) {
                    return true;
                }
            }
            sqlQuery.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username){
        Connection connection = getConnection();

        try {
            PreparedStatement sqlQuery = connection.prepareStatement("DELETE FROM userSchema.userEntity WHERE username = ?");
            sqlQuery.setString(1, username);
            int updatedRows = sqlQuery.executeUpdate();
            sqlQuery.close();
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
        String newHashedPW = PasswordUtility.hashPW(newUserDTO.getPassword());

        try{
            PreparedStatement sqlQuery = connection.prepareStatement("UPDATE userSchema.userEntity SET password = ? WHERE username = ?");
            sqlQuery.setString(1, newHashedPW);
            sqlQuery.setString(2, newUserDTO.getUsername());
            sqlQuery.executeUpdate();
            boolean userExists = (checkUser(newUserDTO));
            sqlQuery.close();
            connection.close();
            return userExists;
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
