package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Tools/")
public class ToolManagement {
    @POST
    @Path("addTool")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addTool(@FormDataParam("toolnameid")Integer toolNameID,
                          @FormDataParam("toolName")String toolName,
                          @FormDataParam("toollist")String toolList,
                          @FormDataParam("username")Integer userID) {

        try {
            if (toolNameID == null || toolName == null || toolList == null || userID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Tools(ToolNameID, ToolName, ToolList, UserID) VALUES (?, ?, ?, ?)");
            ps.setInt(1, toolNameID);
            ps.setString(2, toolName);
            ps.setString(3, toolList);
            ps.setInt(4, userID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String readTool() {
        System.out.println("thing/list");
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT ToolName, ToolList FROM Tools WHERE ToolNameID = ?");
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
    @Path("updateTool")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTool(@FormDataParam("toolname")String toolName,
                             @FormDataParam("toolnameid") Integer toolNameID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Tools SET ToolName = ? WHERE ToolNameID = ?");
            ps.setString(1, toolName);
            ps.setInt(2, toolNameID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("deleteTool")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTool(@FormDataParam("toolid")Integer toolID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Tools WHERE ToolID = ?");
            ps.setInt(1, toolID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
}
