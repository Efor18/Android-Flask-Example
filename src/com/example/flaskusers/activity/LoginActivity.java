package com.example.flaskusers.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flask.R;
import com.example.flaskusers.comm.CommunicationManager;
import com.example.flaskusers.model.User;

public class LoginActivity extends Activity {

	private static final String TAG = "Flask";
	
	private EditText mUserName;
	private EditText mToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mapGUI();
		
	}
	/**
	 * Map all the widget needed from the activity_register.xml layout to activity members.
	 */
	private void mapGUI() {
		mUserName = (EditText) findViewById(R.id.username);
		mToken = (EditText) findViewById(R.id.token);
		
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		String username =  settings.getString(MainActivity.PREFS_USERNAME, "");
		String token = settings.getString(MainActivity.PREFS_ACCESS_TOKEN, "");
		
		mUserName.setText(username);
		mToken.setText(token);
	}
	
	/**
	 * onClick method for the login button
	 */
	public void login(View v) {
		if (checkFields()) {
			String username = mUserName.getText().toString();
			String token = mToken.getText().toString();
			new UpdateTask().execute(username, token);
		} else {
			Toast.makeText(getBaseContext(), R.string.needed_all_fields, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Check if all fields are filled
	 * @return true if all fields are filled
	 */
	private boolean checkFields() {
		boolean result = true;
		String username, token;

		username = mUserName.getText().toString();
		token = mToken.getText().toString();

		if (username.matches("") || token.matches("")) {
			result = false;
		}
		return result;
	}

	private class UpdateTask extends AsyncTask<String, Void, User> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(LoginActivity.this);
			mProgressDialog.setMessage(getResources()
					.getString(R.string.login_progress_signing_in));
			mProgressDialog.show();
		}

		@Override
		protected User doInBackground(String... params) {
			User registered = null;
			try {
				registered = CommunicationManager.getInstance(getBaseContext())
						.loginUser(params[0], params[1]);
			} catch (Exception e) {
				Log.e(TAG, "Error login in: " + e.getMessage());
			}
			return registered;
		}

		@Override
		protected void onPostExecute(User result) {
			mProgressDialog.dismiss();
			if (result != null) {
				Toast.makeText(getBaseContext(), 
						R.string.success_login, Toast.LENGTH_LONG).show();
				
				Intent resultIntent = new Intent(getBaseContext(), DetailActivity.class);
				resultIntent.putExtra("user", result);
				setResult(RESULT_OK, resultIntent);
				
				finish();
			} else {
				Toast.makeText(getBaseContext(), R.string.error_undefined, Toast.LENGTH_LONG).show();
			}
		}
	}
}
