package com.example.flaskusers.comm;

public class CommunicationResponse {
	public boolean success;
	public Object response;
	String responseText;
	public String msgError;

	@Override
	public String toString() {
		return "Success: " + success + " Response: " + responseText
				+ " Msg Error: " + msgError;
	}
}
