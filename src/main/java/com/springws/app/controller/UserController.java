package com.springws.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.springws.app.service.UserService;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
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

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUsers(@PathVariable String id) {
		UserRest user = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, user);
		return user;
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		}

		UserRest returnValue = new UserRest();

		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	@PutMapping(path = "/{id}")
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest user = new UserRest();
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);
		userDto = userService.updateUser(id, userDto);

		BeanUtils.copyProperties(userDto, user);
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
		
		for (UserDto user : users) {
			UserRest userRest = new UserRest();
			BeanUtils.copyProperties(user, userRest);
			returnValue.add(userRest);
		}
		
		return returnValue;
	}
}
