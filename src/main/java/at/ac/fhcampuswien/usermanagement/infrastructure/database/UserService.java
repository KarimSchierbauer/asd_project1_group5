package at.ac.fhcampuswien.usermanagement.infrastructure.database;

import at.ac.fhcampuswien.usermanagement.models.NewUserDTO;
import at.ac.fhcampuswien.usermanagement.util.PasswordUtility;

import java.sql.*;

public class UserService {

    private static final String CONNECTION_BASE_URL = "jdbc:postgresql://localhost:5432/db_usermanager";
    private static final String CONNECTION_USERNAME = "username";
    private static final String CONNECTION_PASSWORD = "passw0rd";
    private static final String USERNAME_COLUMN = "username";
    private static final String PASSWORD_COLUMN = "password";

    private Connection getConnection(){
        try {
            return DriverManager.getConnection(CONNECTION_BASE_URL,
                    CONNECTION_USERNAME, CONNECTION_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
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
            try (Statement connectionStatement = connection.createStatement()) {
                String sqlStatement = String.format(sqlQuery, newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getUsername(), hashPw);
                connectionStatement.executeUpdate(sqlStatement);
            }
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
            try (PreparedStatement sqlQuery = connection.prepareStatement("SELECT username FROM userSchema.userEntity WHERE username = ?")) {
                sqlQuery.setString(1, username);
                ResultSet rs = sqlQuery.executeQuery();
                while (rs.next()) {
                    String usernameFromDb = rs.getString(USERNAME_COLUMN);
                    return usernameFromDb != null && !usernameFromDb.isEmpty();
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(NewUserDTO newUserDTO) {
        Connection connection = getConnection();

        try {
            try (PreparedStatement sqlQuery = connection.prepareStatement("SELECT username, password FROM userSchema.userEntity WHERE username=?")) {
                sqlQuery.setString(1, newUserDTO.getUsername());
                ResultSet rs = sqlQuery.executeQuery();
                while (rs.next()) {
                    String usernameFromDb = rs.getString(USERNAME_COLUMN);
                    if (!usernameFromDb.equals(newUserDTO.getUsername())) {
                        continue;
                    }
                    String passwordFromDb = rs.getString(PASSWORD_COLUMN);
                    if (PasswordUtility.checkPW(newUserDTO.getPassword(), passwordFromDb)) {
                        return true;
                    }
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username){
        Connection connection = getConnection();

        try {
            int updatedRows;
            try (PreparedStatement sqlQuery = connection.prepareStatement("DELETE FROM userSchema.userEntity WHERE username = ?")) {
                sqlQuery.setString(1, username);
                updatedRows = sqlQuery.executeUpdate();
            }
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
            boolean userExists;
            try (PreparedStatement sqlQuery = connection.prepareStatement("UPDATE userSchema.userEntity SET password = ? WHERE username = ?")) {
                sqlQuery.setString(1, newHashedPW);
                sqlQuery.setString(2, newUserDTO.getUsername());
                sqlQuery.executeUpdate();
                userExists = (checkUser(newUserDTO));
            }
            connection.close();
            return userExists;
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
