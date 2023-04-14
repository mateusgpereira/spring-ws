package com.springws.app.ui.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequestModel {

	private String firstName;
	private String lastName;
	@NotBlank(message = "Email is mandatory")
	private String email;
	private String password;
	private List<AddressRequestModel> addresses;

}
