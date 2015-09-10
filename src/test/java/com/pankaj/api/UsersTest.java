package com.pankaj.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
 
import java.net.URISyntaxException;
 
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
 
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import javax.ws.rs.client.Entity;

import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.container.servlet.WebComponent;

import com.pankaj.api.Users;
 
public class UsersTest extends JerseyTest {
	@Override
	protected AppDescriptor configure() {
		return new	WebAppDescriptor.Builder()
					.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
            	            ClassNamesResourceConfig.class.getName())
                	.initParam(
                    	    ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
                        	Users.class.getName()).build();
	}

	//Case 1: Test user registration successfull
	@Test
	public void testUserRegistration() throws JSONException, URISyntaxException {
		
		JSONObject userObject = new JSONObject();
	    userObject.put("userName", "newUser");
	    userObject.put("email", "user2@mail.com");
	    userObject.put("password", "newPassword");
	    
	    ClientResponse response = resource().path("users/register").type("application/json").post(ClientResponse.class, userObject.toString());
        assertEquals(Status.OK, response.getClientResponseStatus());	    
	}

	//Case 2: Test user name registered successfully
	@Test
	public void testUserFetchesSuccess() throws JSONException, URISyntaxException {
		ClientResponse response = resource().path("/users/check").queryParam("name", "newUser").get(ClientResponse.class);
        
        JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());
        assertTrue((Boolean)responseJSON.get("found"));
	}

	//Case 3: Test user registration un-successfull for duplicate user name
	@Test
	public void testDuplicateUserRegistration() throws JSONException, URISyntaxException {
		
		JSONObject userObject = new JSONObject();
	    userObject.put("userName", "newUser");
	    userObject.put("email", "user2@mail.com");
	    userObject.put("password", "newPassword");
	    
	    ClientResponse response = resource().path("users/register").type("application/json").post(ClientResponse.class, userObject.toString());
	   
	    JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());	    
	    assertTrue((Boolean)responseJSON.get("error"));
	    assertEquals("duplicateUserName", (String)responseJSON.get("type"));
	}
	
	//Case 4: Test non-registered user name available or not
	@Test
	public void testUserFetchesFailure() throws JSONException, URISyntaxException {
		ClientResponse response = resource().path("/users/check").queryParam("name", "pankaj123").get(ClientResponse.class);
        
        JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());
        assertEquals(false, (Boolean)responseJSON.get("found"));       
	}

	//Case 5: Validate the registered user with correct credentials
	@Test
	public void testUserValidation() throws JSONException, URISyntaxException {
		JSONObject authObject = new JSONObject();
	    authObject.put("userName", "newUser");
	    authObject.put("password", "newPassword");
	    
	    ClientResponse response = resource().path("users/validate").type("application/json").post(ClientResponse.class, authObject.toString());

	    JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());	    
	    assertTrue((Boolean)responseJSON.get("valid"));
	    assertEquals("user2@mail.com", (String)responseJSON.get("email"));
	}

	//Case 6: Validate the registered user with in-correct credentials
	@Test
	public void testUserValidationWithWrongCredentials() throws JSONException, URISyntaxException {
		JSONObject authObject = new JSONObject();
	    authObject.put("userName", "newUser");
	    authObject.put("password", "newPassword123");
	    
	    ClientResponse response = resource().path("users/validate").type("application/json").post(ClientResponse.class, authObject.toString());

	    JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());	    
	    assertEquals(false, (Boolean)responseJSON.get("valid"));	    
	}

	//Case 7: Validate the un-registered user
	@Test
	public void testUnRegisteredUserValidation() throws JSONException, URISyntaxException {
		JSONObject authObject = new JSONObject();
	    authObject.put("userName", "newUser123");
	    authObject.put("password", "newPassword");
	    
	    ClientResponse response = resource().path("users/validate").type("application/json").post(ClientResponse.class, authObject.toString());

	    JSONObject responseJSON = getResponseAsJSON(response);
        assertEquals(Status.OK, response.getClientResponseStatus());	    
	    assertTrue((Boolean)responseJSON.get("error"));
	    assertEquals(false, (Boolean)responseJSON.get("valid"));	    
	    assertEquals("duplicateUserName", (String)responseJSON.get("type"));
	}

	public JSONObject getResponseAsJSON(ClientResponse response) throws JSONException {
		String entity = response.getEntity(String.class);
        JSONObject responseObject = new JSONObject(entity);
        return responseObject;
	}
}