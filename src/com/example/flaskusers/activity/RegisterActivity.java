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

public class RegisterActivity extends Activity {

	private static final String TAG = "Flask";

	private EditText mUserName;
	private EditText mEmail;
	private EditText mFirstName;
	private EditText mLastName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mapGUI();
	}

	/**
	 * Map all the widget needed from the activity_register.xml layout to activity members.
	 */
	private void mapGUI() {
		mUserName = (EditText) findViewById(R.id.username);
		mEmail = (EditText) findViewById(R.id.email);
		mFirstName = (EditText) findViewById(R.id.first_name);
		mLastName = (EditText) findViewById(R.id.last_name);
	}

	public void registerUser(View id) {

		if (checkFields()) {
			User u = new User()
					.setUsername(mUserName.getText().toString())
					.setEmail(mEmail.getText().toString())
					.setFirstName(mFirstName.getText().toString())
					.setLastName(mLastName.getText().toString());	
			new RegisterTask().execute(u);
		} else {
			Toast.makeText(getBaseContext(), R.string.needed_all_fields, Toast.LENGTH_LONG).show();
		}
	}

	private boolean checkFields() {
		boolean result = true;
		String username, email, firstName, lastName;

		username = mUserName.getText().toString();
		email = mEmail.getText().toString();
		firstName = mFirstName.getText().toString();
		lastName = mLastName.getText().toString();

		if (username.matches("") 
				|| email.matches("") 
				|| firstName.matches("") 
				|| lastName.matches("")) {
			result = false;
		}
		return result;
	}

	private class RegisterTask extends AsyncTask<User, Void, User> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(RegisterActivity.this);
			mProgressDialog.setMessage(getResources().getString(R.string.register_progress));
			mProgressDialog.show();
		}

		@Override
		protected User doInBackground(User... params) {
			User registered = null;
			try {
				registered = CommunicationManager
						.getInstance(getBaseContext()).registerUser(params[0]);
			} catch (Exception e) {
				Log.e(TAG, "Error creating a user: " + e.getMessage());
			}
			return registered;
		}

		@Override
		protected void onPostExecute(User result) {
			mProgressDialog.dismiss();
			if (result != null) {
				Toast.makeText(getBaseContext(), R.string.success_create, Toast.LENGTH_LONG).show();

				SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(MainActivity.PREFS_USERNAME, result.getUsername());
				editor.putString(MainActivity.PREFS_ACCESS_TOKEN, result.getAccessToken());
				editor.commit();
				
				Intent resultIntent = new Intent(getBaseContext(), DetailActivity.class);
				resultIntent.putExtra("user", result);
				setResult(RESULT_OK, resultIntent);
				finish();
			} else {
				Toast.makeText(getBaseContext(), R.string.error_existing_user, Toast.LENGTH_LONG).show();
			}

		}
	}

}
