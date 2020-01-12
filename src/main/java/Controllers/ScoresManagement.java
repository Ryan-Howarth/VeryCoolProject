package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
@Path("Scores/")

public class ScoresManagement {
    @POST
    @Path("addScore")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addScore(@FormDataParam("scoreID")Integer scoreID,
                                @FormDataParam("userID")Integer userID,
                                @FormDataParam("score")Integer score) {
        try {
            if (scoreID == null || userID == null || score == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Scores(ScoreID, UserID, Score) VALUES (?, ?, ?)");
            ps.setInt(1, scoreID);
            ps.setInt(2, userID);
            ps.setInt(3, score);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";

        }
    }

    @GET
    @Path("readScore")
    @Produces(MediaType.APPLICATION_JSON)
    public String readScore() {
        System.out.println("thing/list");
        JSONArray readScores = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT Score FROM Scores");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                readScores.add(item);
            }
            return readScores.toString();


        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("updateScore")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateScore(@FormDataParam("score")String score,
                              @FormDataParam("scoreid")Integer scoreID) {
        try {
            if (score == null || scoreID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Scores SET Score = ? WHERE ScoreID = ?");
            ps.setString(1, score);
            ps.setInt(2, scoreID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("deleteScore")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteScore(@FormDataParam("scoreid")Integer scoreID) throws Exception {
        try {
            if (scoreID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Scores WHERE ScoreID = ?");
            ps.setInt(1, scoreID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
}
