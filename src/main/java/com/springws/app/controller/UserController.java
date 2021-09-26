package com.springws.app.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springws.app.exceptions.UserServiceException;
import com.springws.app.service.AddressService;
import com.springws.app.service.UserService;
import com.springws.app.shared.dto.AddressDto;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
import com.springws.app.ui.model.response.AddressRest;
import com.springws.app.ui.model.response.ErrorMessages;
import com.springws.app.ui.model.response.OperationName;
import com.springws.app.ui.model.response.OperationResponse;
import com.springws.app.ui.model.response.OperationStatus;
import com.springws.app.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUsers(@PathVariable String id) {
		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper mapper = new ModelMapper();
		UserRest user = mapper.map(userDto, UserRest.class);
		return user;
	}

	@PostMapping
	public UserRest createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		}

		UserRest returnValue = new UserRest();

		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		returnValue = mapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}")
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		userDto = userService.updateUser(id, userDto);

		UserRest user = modelMapper.map(userDto, UserRest.class);
		return user;
	}

	@DeleteMapping(path = "/{id}")
	public OperationResponse deleteUser(@PathVariable String id) {
		OperationResponse res = new OperationResponse();
		res.setName(OperationName.DELETE.name());

		userService.deleteUser(id);
		res.setStatus(OperationStatus.SUCCESS.name());
		
		return res;
	}
	
	@GetMapping()
	public List<UserRest> list(
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="limit", defaultValue= "25") int limit) {
		List<UserRest> returnValue = new ArrayList<UserRest>();
		
		List<UserDto> users = userService.list(page, limit);
		ModelMapper mapper = new ModelMapper();
		
		for (UserDto user : users) {
			UserRest userRest = mapper.map(user, UserRest.class);
			returnValue.add(userRest);
		}
		
		return returnValue;
	}
	
	@GetMapping(path = "/{id}/addresses")
	public List<AddressRest> getAddresses(@PathVariable String id){
		List<AddressRest> returnValue = new ArrayList<>();
		
		List<AddressDto> addressesDto = addressService.getAddresses(id);
		
		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		return returnValue;
	}
	
	@GetMapping(path = "/{id}/addresses/{addressId}")
	public EntityModel<AddressRest> getUserAddress(@PathVariable String id, @PathVariable String addressId){
		
		AddressDto addressDto = addressService.getAddressById(addressId);
		
		if (addressDto == null) {
			return null;
		}
		
		AddressRest userAddress = new ModelMapper().map(addressDto, AddressRest.class);
		
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(id)
				.withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(id)
				.slash("addresses")
				.withRel("addresses");
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(id)
				.slash("addresses")
				.slash(addressId)
				.withSelfRel();
		
		return EntityModel.of(userAddress, Arrays.asList(userLink, userAddressesLink, selfLink));
	}
}
