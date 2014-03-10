package com.example.flaskusers.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
	
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
    private int mId;
	private String mUsername;
	private String mAccessToken;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	
	public User() { }
	
	protected User(Parcel in) {
        mId = in.readInt();
        mUsername = in.readString();
        mAccessToken = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
    }
	
	public int getId() {
		return mId;
	}
	public User setId(int id) {
		this.mId = id;
		return this;
	}
	public String getUsername() {
		return mUsername;
	}
	public User setUsername(String username) {
		this.mUsername = username;
		return this;
	}
	public String getAccessToken() {
		return mAccessToken;
	}
	public User setAccessToken(String accessToken) {
		this.mAccessToken = accessToken;
		return this;
	}
	public String getFirstName() {
		return mFirstName;
	}
	public User setFirstName(String firstName) {
		this.mFirstName = firstName;
		return this;
	}
	public String getLastName() {
		return mLastName;
	}
	public User setLastName(String lastName) {
		this.mLastName = lastName;
		return this;
	}
	public String getEmail() {
		return mEmail;
	}
	public User setEmail(String email) {
		this.mEmail = email;
		return this;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mUsername);
        dest.writeString(mAccessToken);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
    }
}
