package com.pankaj.api;
 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.pankaj.api.UserService;

 
@Path("/users")
public class Users {

		
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response writeUserName(String requestObject) throws IOException, JSONException {
		
		JSONObject jsonObject = new JSONObject(requestObject);
		JSONObject resultObject = new JSONObject();
		try {
			if(UserService.checkUserName((String)jsonObject.get("userName"))) {
				resultObject.put("error", true);
				resultObject.put("type", "duplicateUserName");
				resultObject.put("message", "User Name is not available");
			} else {
				UserService.writeUserInfo(jsonObject);	
			}			
		} catch (JSONException e) {
			resultObject.put("error", true);
			resultObject.put("type", "requestParsing");
			resultObject.put("message", "There was an error, parsing the request");
		} catch (IOException e) {
			resultObject.put("error", true);
			resultObject.put("type", "updateRecord");
			resultObject.put("message", "There was an error, updating the records");
		}
		
		
 		String result = "" + resultObject;
		return Response.status(200).entity(result).build();
	}

	@Path("/check")
	@GET
	@Produces("application/json")
	public Response checkUserName(@QueryParam("name") String name) throws IOException, JSONException{
		
		JSONObject jsonObject = new JSONObject();
		
		boolean found = UserService.checkUserName(name);		
		jsonObject.put("found", found);  		
		String result = "" + jsonObject;
		return Response.status(200).entity(result).build(); 		
	}

	@Path("/validate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validateUser(String requestObject) throws IOException, JSONException {
		
		JSONObject jsonObject = new JSONObject(requestObject);
		JSONObject resultObject = new JSONObject();
		try {
			if(UserService.checkUserName((String)jsonObject.get("userName"))) {
				resultObject = UserService.checkUserCredentials(jsonObject);				
			} else {
				resultObject.put("error", true);
				resultObject.put("valid", false);
				resultObject.put("type", "duplicateUserName");
				resultObject.put("message", "User Name is not available");
			}			
		} catch (JSONException e) {
			resultObject.put("error", true);
			resultObject.put("type", "requestParsing");
			resultObject.put("message", "There was an error, parsing the request");
		} catch (IOException e) {
			resultObject.put("error", true);
			resultObject.put("type", "updateRecord");
			resultObject.put("message", "There was an error, updating the records");
		}
		
		
 		String result = "" + resultObject;
		return Response.status(200).entity(result).build();
	}
	
}




