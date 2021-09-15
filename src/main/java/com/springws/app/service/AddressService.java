package com.springws.app.service;

import java.util.List;

import com.springws.app.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);
	AddressDto getAddressById(String id);

}
