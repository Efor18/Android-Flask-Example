package com.example.flaskusers.comm;

import android.content.Context;
import android.util.Log;
 
import com.example.flaskusers.model.User;

import org.json.JSONObject;



public class CommunicationManager {
	private static final String TAG = "Comm";

	private static final String URL_PATH = "http://10.0.3.2:5000/mymusic/api/v1.0/users/";

	private static Context sContext;
	private static CommunicationManager sINSTANCE = null;
	private CommUtils mUtil = new CommUtils();


	private static synchronized void createInstance(Context context) {
		if (sINSTANCE == null) {
			Log.d(TAG, "URL: " + URL_PATH);
			sINSTANCE = new CommunicationManager();
			sContext = context;
		}
	}

	public static CommunicationManager getInstance(Context ctx) {
		if (sINSTANCE == null) {
			createInstance(ctx);
		}
		return sINSTANCE;
	}

	public User registerUser(User user) throws Exception {
		CommunicationResponse res;
		User result = null;
		JSONObject json = JSONManager.getJSONCreateUser(user);
		if (json != null) {
			res = mUtil.post(URL_PATH, json);
			if (res.success) {
				result = JSONManager.processRegister(sContext, res.responseText);
			} else {
				Log.e(TAG, "Error registering user: " + res.msgError);
			}		
		}
		return result;
	}

	public User loginUser(String userName, String token) throws Exception {
		CommunicationResponse res;
		User result = null;
		res = mUtil.get(URL_PATH, userName, token);
		if (res.success) {
			result = JSONManager.processLogin(sContext, res.responseText);
		} else {
			Log.e(TAG, "Error registering user: " + res.msgError);
		}		
		return result;
	}

	public boolean updateUser(User user) throws Exception {
		CommunicationResponse res = null;
		JSONObject json = JSONManager.getJSONUpdateUser(user);
		if (json != null) {
			res = mUtil.put(URL_PATH, json, user);	
		}
		return res.success;
	}

	public boolean deleteUser(User user) {
		CommunicationResponse res;
		res = mUtil.delete(URL_PATH, user);	
		return res.success;
	}
}
