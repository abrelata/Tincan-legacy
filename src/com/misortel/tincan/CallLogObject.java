package com.misortel.tincan;

public class CallLogObject {
	private String contact, duration;
	
	public void setContact(String contact){
		this.contact = contact;
	}
	public void setDuration(String duration){
		this.duration = duration;
	}
	public String getContact(){
		return contact;
	}
	public String getDuration(){
		return duration;
	}
}