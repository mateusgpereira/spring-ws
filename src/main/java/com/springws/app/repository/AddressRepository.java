package com.springws.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springws.app.entities.AddressEntity;
import com.springws.app.entities.UserEntity;

@Repository
public interface AddressRepository  extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
