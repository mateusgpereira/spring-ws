package com.springws.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springws.app.entities.UserEntity;
import com.springws.app.exceptions.UserServiceException;
import com.springws.app.repository.UserRepository;
import com.springws.app.service.UserService;
import com.springws.app.shared.Utils;
import com.springws.app.shared.dto.AddressDto;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.response.ErrorMessages;

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
		if (userExists != null) {
			throw new RuntimeException("User already exists");
		}
		
		for (int i = 0 ; i < user.getAddresses().size(); i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}
		
		ModelMapper mapper = new ModelMapper();
		UserEntity entity = mapper.map(user, UserEntity.class);

		entity.setEncryptedPassword(bCrypt.encode(user.getPassword()));
		entity.setUserId(utils.generateUserId(15));

		UserEntity stored = repository.save(entity);

		return mapper.map(stored, UserDto.class);
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

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserEntity entity = repository.findByUserId(userId);

		if (entity == null) {
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		entity.setFirstName(user.getFirstName());
		entity.setLastName(user.getLastName());

		entity = repository.save(entity);
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(entity, userDto);

		return userDto;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity entity = repository.findByUserId(userId);

		if (entity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		repository.delete(entity);
	}

	@Override
	public List<UserDto> list(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<UserDto>();
		
		Pageable pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> userPage = repository.findAll(pageRequest);
		List<UserEntity> users = userPage.getContent();
		
		for (UserEntity entity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(entity, userDto);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}
}
