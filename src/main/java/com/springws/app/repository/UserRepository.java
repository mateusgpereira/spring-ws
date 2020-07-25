package com.springws.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springws.app.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	public UserEntity findByEmail(String email);
	public UserEntity findByUserId(String userId);

}
