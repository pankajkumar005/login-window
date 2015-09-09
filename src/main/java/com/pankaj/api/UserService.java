package com.pankaj.api;
 
import javax.ws.rs.core.Response; 
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 
public class UserService {
	
	public static void writeUserInfo(JSONObject jsonObject) throws IOException, JSONException {
		
		String userName = jsonObject.get("userName").toString();
		String password = jsonObject.get("password").toString();
		String email = jsonObject.get("email").toString();
		
		//Add User info
		File file = new File("userinfo.txt");
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(userName + "," + password + "," + email);
		
		bw.newLine();
		bw.close();		
	}

	public static boolean checkUserName(String userName) throws IOException, JSONException{
		boolean found = false;
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader("userinfo.txt"));
		while ((sCurrentLine = br.readLine()) != null) {
			String[] userinfo = sCurrentLine.split(",");
			String name = userinfo[0];
			if(name != null  && name.equals(userName)) {
				found = true;
				break;				
			}			
		}
		return found;
	}

	public static JSONObject checkUserCredentials(JSONObject jsonObject) throws IOException, JSONException{
		String userName = jsonObject.get("userName").toString();
		String password = jsonObject.get("password").toString();
		String email = "";
		
		boolean valid = false;
		boolean found = false;
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader("userinfo.txt"));
		while ((sCurrentLine = br.readLine()) != null) {
			String[] userinfo = sCurrentLine.split(",");
			String name = userinfo[0];
			if(name != null  && name.equals(userName)) {
				String pwd = userinfo[1];
				if(pwd != null  && pwd.equals(password)) {
					valid = true;
					email = userinfo[2];
					break;
				}				
			}			
		}
		JSONObject resultObject = new JSONObject();
		resultObject.put("valid", valid);
		if(valid) {
			resultObject.put("email", email);
			resultObject.put("userName", userName);
		} else {

		}
		return resultObject;
	}
}




