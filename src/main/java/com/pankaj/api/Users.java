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

/**
* @author Pankaj Kumar
*/
 
@Path("/users")
public class Users {


	/**
	 * This method is for registering the user information. This is
	 * a REST api with POST method. The url path is "users/register"
	 * and the data to be posted is in json format. For example, if the request is being 
	 * posted via jQuery:
	 * <p>
	 * <pre>
	 * {@code
	 * $.ajax({ 
     *          type        :   'POST'  ,
     *          url         :   '/api/users/register',
     *          data        :   JSON.stringify({
     *                          	userName: <someName>,
     *                              password: <pwd>,
     *                            	email: <email@dom.suf>
     *                         }),
     *          contentType :   'application/json',
     *          dataType    :   'json',
     *      	success     :   function(resp) {
*							if(!resp.error) {
*								//Registration Successfull
	*						}
     *                        }
     *});
     * }
     * </pre>
     * <p> The response returned, will be blank in case of successfull user registration.
     * Otherwise, it contains 'error' field as true and 'message' field, containing the relevent error message.
	 * 
 	*/	
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response writeUserInfo(String requestObject) throws IOException, JSONException {
		
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

	/**
	 * This method is for checking if the given user name is registered or not. This is
	 * a REST api with GET method. The url path is "users/check". For example, if the request is being 
	 * posted via jQuery:
	 * <p>
	 * <pre>
	 * {@code
	 * $.get('/RESTfulExample/api/users/check?name=someName', function(data){
    *    		if(data.found) {
	*				// User name is registered
    *    		} else {
	*				// User name is not registered
    *			}
    *    
    *  });
     * }
     * </pre>
     * <p> The response returned, contains 'found' field as true if user is registered
     * or false if user is not registered.
	 * 
 	*/

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

	/**
	 * This method is for validating the user credentials. This is
	 * a REST api with POST method. The url path is "users/validate"
	 * and the data to be posted is in json format. For example, if the request is being 
	 * posted via jQuery:
	 * <p>
	 * <pre>
	 * {@code
	 * $.ajax({ 
     *          type        :   'POST'  ,
     *          url         :   '/api/users/register',
     *          data        :   JSON.stringify({
     *                          	userName: <someName>,
     *                              password: <pwd>
     *                         }),
     *          contentType :   'application/json',
     *          dataType    :   'json',
     *      	success     :   function(resp) {
	*						if(!resp.error) {
	*							if(resp.valid) {
	*								//registration successfull
	*							} else {
	*								// Login failure, credentials wrong
	*							}
	*						} else {
	*						// Login failure, see resp.message
	*						}
     *                   }
     *});
     * }
     * </pre>
     * <p> The response returned, will have filed 'valid' as true in case of successfull 
     * login. In case of error, it contains 'error' field
     * as true and 'message' field, containing the relevent error message.
	 * 
 	*/

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




