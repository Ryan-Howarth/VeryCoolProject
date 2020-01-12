package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.UUID;

@Path ("user/")
public class UserManagement {

    @POST
    @Path("adduser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public String addUser(@FormDataParam("userid") Integer userID,
                          @FormDataParam("firstname") String firstName,
                          @FormDataParam("lastname") String lastName,
                          @FormDataParam("username") String userName,
                          @FormDataParam("password") String password, @CookieParam("token") String token) {

        if (!UserManagement.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (userID == null || firstName == null || lastName == null|| userName == null|| password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(UserID, FirstName, LastName, UserName, Password) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, userID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, userName);
            ps.setString(5, password);
            ps.execute();
            return "{\"status\": \"OK\"}";


        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";

        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)



    public String readUser() {
        System.out.println("thing/list");
        JSONArray list = new JSONArray();

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT FirstName FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                list.add(item);
            }
            return list.toString();


        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";

        }
    }

    @POST
    @Path("updateuser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public String updateUser(@FormDataParam("firstname") String firstName,
                             @FormDataParam("userid") Integer userID,
                             @CookieParam("token") String token) {

        if (!UserManagement.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }

        try {
            if (firstName == null || userID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET FirstName = ? WHERE UserID = ?");
            ps.setString(1, firstName);
            ps.setInt(2, userID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";


        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("deleteuser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public String deleteUser(@FormDataParam("userid") Integer userID, @CookieParam("token") String token) {

        if (!UserManagement.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }



        try {
            if (userID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, userID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";


        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";

        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {

        try {

            System.out.println("users/login");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE UserName = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);
                if (password.equals(correctPassword)) {

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE UserName = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();

                } else {
                    return "{\"error\": \"Incorrect password!\"}";
                }

            } else {
                return "{\"error\": \"Unknown user!\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }
}


