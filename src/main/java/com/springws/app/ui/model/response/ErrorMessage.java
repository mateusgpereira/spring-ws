package com.springws.app.ui.model.response;

import java.util.Date;

public class ErrorMessage {

	private Date dateTime;
	private String message;
	
	public ErrorMessage() {}
	
	public ErrorMessage(Date dateTime, String message) {
		this.dateTime = dateTime;
		this.message = message;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
