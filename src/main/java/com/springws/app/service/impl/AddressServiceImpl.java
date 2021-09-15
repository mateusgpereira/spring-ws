package com.springws.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springws.app.entities.AddressEntity;
import com.springws.app.entities.UserEntity;
import com.springws.app.repository.AddressRepository;
import com.springws.app.repository.UserRepository;
import com.springws.app.service.AddressService;
import com.springws.app.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			return returnValue;
		}
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		ModelMapper mapper = new ModelMapper();
		for (AddressEntity addressEntity:addresses) {
			returnValue.add(mapper.map(addressEntity, AddressDto.class));
		}

		return returnValue;
	}

	@Override
	public AddressDto getAddressById(String id) {
		AddressEntity addressEntity = addressRepository.findByAddressId(id);
		
		if (addressEntity != null) {
			return new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return null;
	}

}
