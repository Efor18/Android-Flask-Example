package com.example.flaskusers.comm;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

import com.example.flaskusers.model.User;

public class CommUtils {

	private static final String TAG = "Comm";

	private static final int STATUS_OK = 200;
	private static final int STATUS_MULTIPLE_CHOICES = 300;

	
	public CommunicationResponse put(String url, List<NameValuePair> attrs,	String authToken) {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url);
		CommunicationResponse res = new CommunicationResponse();
		try {
			if (attrs != null) {
				httpput.setEntity(new UrlEncodedFormEntity(attrs, "UTF-8"));
			}
			httpput.addHeader("Authorization", "Token token=" + authToken);

			HttpResponse response = client.execute(httpput);

			res = handleResponse(response);

		} catch (UnsupportedEncodingException e) {
			res.success = false;
			res.msgError = "Error UnsupportedEncodingException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (ClientProtocolException e) {
			res.success = false;
			res.msgError = "Error ClientProtocolException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (IOException e) {
			res.success = false;
			res.msgError = "Error IOException: " + e.getMessage();
			Log.e(TAG, res.toString());
		}

		return res;
	}

	public CommunicationResponse get(String url, String userName, String token) {

		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		CommunicationResponse res = new CommunicationResponse();

		try {

			httpget.addHeader("api_access_token", token);
			httpget.addHeader("api_username", userName);

			HttpResponse response = client.execute(httpget);

			res = handleResponse(response);

		} catch (UnsupportedEncodingException e) {
			res.success = false;
			res.msgError = "Error UnsupportedEncodingException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (ClientProtocolException e) {
			res.success = false;
			res.msgError = "Error ClientProtocolException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (IOException e) {
			res.success = false;
			res.msgError = "Error IOException: " + e.getMessage();
			Log.e(TAG, res.toString());
		}

		return res;

	}


	public CommunicationResponse post(String url, JSONObject json) {
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		CommunicationResponse res = new CommunicationResponse();

		try {
			StringEntity se = new StringEntity(json.toString());
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(httppost);
			res = handleResponse(response);

		} catch (UnsupportedEncodingException e) {
			res.success = false;
			res.msgError = "Error UnsupportedEncodingException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (ClientProtocolException e) {
			res.success = false;
			res.msgError = "Error ClientProtocolException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (IOException e) {
			res.success = false;
			res.msgError = "Error IOException: " + e.getMessage();
			Log.e(TAG, res.toString());
		}

		return res;
	}
	
	public CommunicationResponse put(String url, JSONObject json, User user) {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url);
		CommunicationResponse res = new CommunicationResponse();

		try {
			StringEntity se = new StringEntity(json.toString());
			httpput.setEntity(se);
			httpput.addHeader("api_access_token", user.getAccessToken());
			httpput.addHeader("api_username", user.getUsername());
			HttpResponse response = client.execute(httpput);
			res = handleResponse(response);

		} catch (UnsupportedEncodingException e) {
			res.success = false;
			res.msgError = "Error UnsupportedEncodingException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (ClientProtocolException e) {
			res.success = false;
			res.msgError = "Error ClientProtocolException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (IOException e) {
			res.success = false;
			res.msgError = "Error IOException: " + e.getMessage();
			Log.e(TAG, res.toString());
		}

		return res;
	}

	public CommunicationResponse delete(String url, User user) {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(url);
		CommunicationResponse res = new CommunicationResponse();
		try {
			httpdelete.addHeader("api_access_token", user.getAccessToken());
			httpdelete.addHeader("api_username", user.getUsername());
			HttpResponse response = client.execute(httpdelete);
			res = handleResponse(response);
		} catch (UnsupportedEncodingException e) {
			res.success = false;
			res.msgError = "Error UnsupportedEncodingException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (ClientProtocolException e) {
			res.success = false;
			res.msgError = "Error ClientProtocolException: " + e.getMessage();
			Log.e(TAG, res.toString());
		} catch (IOException e) {
			res.success = false;
			res.msgError = "Error IOException: " + e.getMessage();
			Log.e(TAG, res.toString());
		}

		return res;

	}


	private CommunicationResponse handleResponse(HttpResponse response)
		throws IOException {
		CommunicationResponse res = new CommunicationResponse();
		res.success = checkStatus(response.getStatusLine().getStatusCode());

		if (res.success) {
			res.responseText = readAnswer(response.getEntity().getContent());
		} else {
			res.msgError = readAnswer(response.getEntity().getContent());
		}

		if (res.success) {
			Log.i(TAG, "Response: " + res);
		} else {
			Log.e(TAG, "Response: " + res);
		}

		return res;
	}


	private boolean checkStatus(int statusCode) {
		return STATUS_OK <= statusCode && statusCode < STATUS_MULTIPLE_CHOICES;
	}

	private String readAnswer(InputStream is) throws IOException {
		is = new DoneHandlerInputStream(is);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		in.close();
		return sb.toString();

	}


	private class DoneHandlerInputStream extends FilterInputStream {
		private boolean mDone;

		public DoneHandlerInputStream(InputStream stream) {
			super(stream);
		}

		@Override
		public int read(byte[] bytes, int offset, int count) throws IOException {
			if (!mDone) {
				int result = super.read(bytes, offset, count);
				if (result != -1) {
					return result;
				}
			}
			mDone = true;
			return -1;
		}
	}
}
