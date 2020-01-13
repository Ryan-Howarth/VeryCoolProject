package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

@Path("Tasks/")
public class TaskManagement {
    @POST
    @Path("addTask")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addTask(@FormDataParam("taskid")Integer taskID,
                          @FormDataParam("task")String task,
                          @FormDataParam("answer")String answer,
                          @FormDataParam("fake1")String fake1,
                          @FormDataParam("fake2")String fake2,
                          @FormDataParam("fake3")String fake3) {
        try {
            if (taskID == null || task == null || answer == null||fake1  == null||fake2  == null||fake3  == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Tasks(TaskID, Task, CorrectAnswer, Fake1, Fake2, Fake3) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, taskID);
            ps.setString(2, task);
            ps.setString(3, answer);
            ps.setString(4, fake1);
            ps.setString(5, fake2);
            ps.setString(6, fake3);
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
    public String readTask() {
        System.out.println("thing/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Task FROM Tasks");
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
    @Path("updateTask")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTask(@FormDataParam("task")String task,
                             @FormDataParam("taskid")Integer taskID) {
        try {
            if (task == null || taskID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Tasks SET Task = ? WHERE TaskID = ?");
            ps.setString(1, task);
            ps.setInt(2, taskID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("deleteTask")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteTask(@FormDataParam("tasklist")String[] taskList) {

        Integer[] newtasklist=stringToList(taskList);

        for(int i=0;i<newtasklist.length; i++){
            try {
                if (taskList == null) {
                    throw new Exception("One or more form data parameters are missing in the HTTP request.");
                }
                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Tasks WHERE TaskID = ?");
                ps.setInt(1, newtasklist[i]);
                ps.executeUpdate();
                return "{\"status\": \"OK\"}";

            } catch (Exception exception) {
                System.out.println("Database disconnection error: " + exception.getMessage());
                return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
            }
        }
        return null;
    }

    public static Integer[] stringToList(String[] strings) {
        Integer[] intarray=new Integer[strings.length];
        int i=0;
        for(String str:strings){
            intarray[i]=Integer.parseInt(str.trim());//Exception in this line
            i++;
        }
        return intarray;
    }
}
