package com.springws.app.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springws.app.entities.UserEntity;
import com.springws.app.repository.UserRepository;
import com.springws.app.service.UserService;
import com.springws.app.shared.Utils;
import com.springws.app.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCrypt;

	public UserDto createUser(UserDto user) {

		UserEntity userExists = repository.findByEmail(user.getEmail());
		if (userExists != null)
			throw new RuntimeException("User already exists");

		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(user, entity);

		entity.setEncryptedPassword(bCrypt.encode(user.getPassword()));
		entity.setUserId(utils.generateUserId(15));

		UserEntity stored = repository.save(entity);

		BeanUtils.copyProperties(stored, user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = repository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) throws UsernameNotFoundException {
		UserEntity user = repository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) throws UsernameNotFoundException {
		UserDto userDto = new UserDto();
		UserEntity user = repository.findByUserId(userId);
		
		if (user == null) {
			throw new UsernameNotFoundException(userId);
		}
		
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}
}
