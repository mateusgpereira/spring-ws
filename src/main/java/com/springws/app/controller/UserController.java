package com.springws.app.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springws.app.service.UserService;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
import com.springws.app.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path="/{id}")
	public UserRest getUsers(@PathVariable String id) {
		UserRest user = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, user);
		return user;
	}
	
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping
	public String updateUser() {
		return "update user was called";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "delete user was called";
	}
}
