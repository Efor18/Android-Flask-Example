package com.example.flaskusers.comm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.flaskusers.model.User;

public class JSONManager {

	private static final String TAG = "Comm";

	private static final String ATTR_USER = "user";
	private static final String ATTR_ID = "id";
	private static final String ATTR_USERNAME = "username";
	private static final String ATTR_ACCESS_TOKEN = "access_token";
	private static final String ATTR_EMAIL = "email";
	private static final String ATTR_FIRST_NAME = "first_name";
	private static final String ATTR_LAST_NAME = "last_name";


	public static User processRegister(Context ctx, String responseText) throws Exception {
		
		User result = new User();
		JSONObject jsonObject = new JSONObject(responseText).getJSONObject(ATTR_USER);
		result.setId(jsonObject.getInt(ATTR_ID))
			.setUsername(jsonObject.getString(ATTR_USERNAME))
			.setAccessToken(jsonObject.getString(ATTR_ACCESS_TOKEN))
			.setEmail(jsonObject.getString(ATTR_EMAIL))
			.setFirstName(jsonObject.getString(ATTR_FIRST_NAME))
			.setLastName(jsonObject.getString(ATTR_LAST_NAME));

		return result;
	}

	public static User processLogin(Context mContext, String responseText) throws Exception {
		User result = new User();

		JSONObject jsonObject = new JSONObject(responseText);
		result.setId(jsonObject.getInt(ATTR_ID))
			.setUsername(jsonObject.getString(ATTR_USERNAME))
			.setAccessToken(jsonObject.getString(ATTR_ACCESS_TOKEN))
			.setEmail(jsonObject.getString(ATTR_EMAIL))
			.setFirstName(jsonObject.getString(ATTR_FIRST_NAME))
			.setLastName(jsonObject.getString(ATTR_LAST_NAME));

		return result;
	}


	public static JSONObject getJSONCreateUser(User user) {
		
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put(ATTR_USERNAME, user.getUsername());
			jsonObject.put(ATTR_FIRST_NAME, user.getFirstName());
			jsonObject.put(ATTR_LAST_NAME, user.getLastName());
			jsonObject.put(ATTR_EMAIL, user.getEmail());
		} catch (JSONException e) {
			Log.e(TAG, "Error generating JSON for user creation");
		}
		return jsonObject;
	}


	public static JSONObject getJSONUpdateUser(User user) {

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			if(user.getFirstName()!=null) jsonObject.put(ATTR_FIRST_NAME, user.getFirstName());
			if(user.getLastName()!=null) jsonObject.put(ATTR_LAST_NAME, user.getLastName());
			if(user.getEmail()!=null) jsonObject.put(ATTR_EMAIL, user.getEmail());
		} catch (JSONException e) {
			Log.e(TAG, "Error generating JSON for user update");
		}
		return jsonObject;
	}
}
