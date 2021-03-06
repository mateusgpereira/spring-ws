package com.springws.app.ui.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class UserDetailsRequestModel {

	private String firstName;
	private String lastName;
	@NotBlank(message = "Email is mandatory")
	private String email;
	private String password;
	private List<AddressRequestModel> addresses;

	public String getFirstName() {
		return firstName;
	}

	public List<AddressRequestModel> getaddresses() {
		return addresses;
	}

	public void setaddresses(List<AddressRequestModel> addresses) {
		this.addresses = addresses;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
