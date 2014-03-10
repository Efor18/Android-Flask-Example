package com.example.flaskusers.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flask.R;
import com.example.flaskusers.comm.CommunicationManager;
import com.example.flaskusers.model.User;

public class DetailActivity extends Activity {

	private static final String TAG = "Flask";

	private User mUser;
	private TextView mUserId;
	private TextView mUserToken;
	private TextView mUserName;
	private EditText mEmail;
	private EditText mFirstName;
	private EditText mLastName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mUser = getIntent().getParcelableExtra("user");
		mapGUI();
		fillData();
	}

	/**
	 * Map all the widget needed from the activity_register.xml layout to activity members.
	 */
	private void mapGUI() {
		mUserId = (TextView) findViewById(R.id.user_id);
		mUserToken = (TextView) findViewById(R.id.user_token);
		mUserName = (TextView) findViewById(R.id.username);
		mEmail = (EditText) findViewById(R.id.email);
		mFirstName = (EditText) findViewById(R.id.first_name);
		mLastName = (EditText) findViewById(R.id.last_name);
		
	}

	/**
	 * Fill the users data
	 */
	private void fillData() {
		mUserId.setText(String.valueOf(mUser.getId()));
		mUserToken.setText(mUser.getAccessToken());
		mUserName.setText(mUser.getUsername());
		mEmail.setText(mUser.getEmail());
		mFirstName.setText(mUser.getFirstName());
		mLastName.setText(mUser.getLastName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		new DeleteTask().execute(mUser);
		return true;
	}
	
	/**
	 * onClick method for the update button
	 */
	public void update(View v) {
		if (checkFields()) {
			User user = getChanges();
			new UpdateTask().execute(user);
		} else {
			Toast.makeText(getBaseContext(), R.string.change_for_update, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Creates a new User object with the changed values
	 * @return a User with the new changes
	 */
	private User getChanges() {
		User user = new User()
				.setUsername(mUser.getUsername())
				.setAccessToken(mUser.getAccessToken());
		
		String email, firstName, lastName;
		email = mEmail.getText().toString();
		firstName = mFirstName.getText().toString();
		lastName = mLastName.getText().toString();
		
		if (!email.equals(mUser.getEmail())) {
			user.setEmail(email);
		}
		
		if (!firstName.equals(mUser.getFirstName())) {
			user.setFirstName(firstName);
		}
		
		if (!lastName.equals(mUser.getLastName())) {
			user.setLastName(lastName);
		}
		
		return user;
	}

	/**
	 * Check if is any changes in the data
	 * @return true if there are changed data
	 */
	private boolean checkFields() {
		boolean result = false;
		String email, firstName, lastName;

		email = mEmail.getText().toString();
		firstName = mFirstName.getText().toString();
		lastName = mLastName.getText().toString();

		if (!email.equals(mUser.getEmail()) 
				|| !firstName.equals(mUser.getFirstName()) 
				|| !lastName.equals(mUser.getLastName())) {
			result = true;
		}
		return result;
	}
	
	

	private class DeleteTask extends AsyncTask<User, Void, Boolean> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(DetailActivity.this);
			mProgressDialog.setMessage(getResources().getString(R.string.delete_progress));
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(User... params) {
			boolean deleted = false;
			try {
				deleted = CommunicationManager.getInstance(getBaseContext()).deleteUser(params[0]);
			} catch (Exception e) {
				Log.e(TAG, "Error deleting a user: " + e.getMessage());
			}
			return deleted;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				
				SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();
				
				finish();
				Toast.makeText(getBaseContext(), R.string.success_delete, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getBaseContext(), R.string.error_undefined, Toast.LENGTH_LONG).show();
			}
		}
	}

	private class UpdateTask extends AsyncTask<User, Void, Boolean> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(DetailActivity.this);
			mProgressDialog.setMessage(getResources().getString(R.string.update_progress));
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(User... params) {

			boolean success = false;
			try {
				success = CommunicationManager.getInstance(getBaseContext()).updateUser(params[0]);
			} catch (Exception e) {
				Log.e(TAG, "Error updating a user: " + e.getMessage());
			}
			return success;
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				Toast.makeText(getBaseContext(), R.string.success_update, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getBaseContext(), R.string.error_updating, Toast.LENGTH_LONG).show();
			}
		}
	}
}
