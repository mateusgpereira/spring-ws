package com.springws.app.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springws.app.UserEntity;
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
		if(userExists != null) throw new RuntimeException("User already exists");
		
		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(user, entity);
		
		entity.setEncryptedPassword(bCrypt.encode(user.getPassword()));
		entity.setUserId(utils.generateUserId(15));
		
		UserEntity stored = repository.save(entity);
		
		BeanUtils.copyProperties(stored, user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
