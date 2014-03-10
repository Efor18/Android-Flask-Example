package com.example.flaskusers.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.flask.R;

public class MainActivity extends Activity {
	public static final int REQ_CODE_REGISTER = 10;
	public static final int REQ_CODE_LOGIN = 11;
	public static final String PREFS_NAME = "MyPrefsFile";

	public static final String PREFS_USERNAME = "username";
	public static final String PREFS_ACCESS_TOKEN = "access_token";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void login(View id) {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String username =  settings.getString(PREFS_USERNAME, null);
		if (username == null) {
			Toast.makeText(getBaseContext(), 
					R.string.needed_user, Toast.LENGTH_LONG).show();
		} else {
			Intent i = new Intent(this, LoginActivity.class);
			startActivityForResult(i, REQ_CODE_LOGIN);
		}
	}

	public void register(View id) {
		Intent i = new Intent(this, RegisterActivity.class);
		startActivityForResult(i, REQ_CODE_REGISTER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			startActivity(data);
		}
	}
}
