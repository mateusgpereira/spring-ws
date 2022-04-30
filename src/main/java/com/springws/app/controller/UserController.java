package com.springws.app.controller;

import com.springws.app.exceptions.UserServiceException;
import com.springws.app.service.AddressService;
import com.springws.app.service.UserService;
import com.springws.app.shared.dto.AddressDto;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.PasswordResetRequestModel;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
import com.springws.app.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public List<UserRest> list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "25") int limit) {
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
	public CollectionModel<AddressRest> getAddresses(@PathVariable String id) {
		List<AddressRest> returnValue = new ArrayList<>();

		List<AddressDto> addressesDto = addressService.getAddresses(id);

		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		
		for (AddressRest address : returnValue) {
			Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, address.getAddressId()))
					.withSelfRel();
			address.add(selfLink);
		}

		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("users");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(id))
				.withSelfRel();
		return CollectionModel.of(returnValue, userLink, selfLink);
	}

	@GetMapping(path = "/{id}/addresses/{addressId}")
	public EntityModel<AddressRest> getUserAddress(@PathVariable String id, @PathVariable String addressId) {

		AddressDto addressDto = addressService.getAddressById(addressId);

		if (addressDto == null) {
			return null;
		}

		AddressRest userAddress = new ModelMapper().map(addressDto, AddressRest.class);

		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(id)).withRel("addresses");
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, addressId)).withSelfRel();

		return EntityModel.of(userAddress, Arrays.asList(userLink, userAddressesLink, selfLink));
	}

	@GetMapping(path = "/verification-email")
	public OperationResponse verifyEmailToken(@RequestParam(value = "token") String token) {
		OperationResponse operationResult = new OperationResponse();
		operationResult.setName(OperationName.VERIFY_EMAIL.name());
		
		if (userService.verifyEmailToken(token)) {
			operationResult.setStatus(OperationStatus.SUCCESS.name());
		} else {
			operationResult.setStatus(OperationStatus.ERROR.name());
		}
		
		return operationResult;
	}

	@PostMapping("/password-reset-request")
	public OperationResponse requestReset(@RequestBody PasswordResetRequestModel requestModel) {
		OperationResponse operationResponse = new OperationResponse();

		boolean operationResult = userService.requestPasswordReset(requestModel.getEmail());
		operationResponse.setName(OperationName.REQUEST_PASSWORD_RESET.name());
		operationResponse.setStatus(operationResult ? OperationStatus.SUCCESS.name() : OperationStatus.ERROR.name());
		return operationResponse;
	}
}
