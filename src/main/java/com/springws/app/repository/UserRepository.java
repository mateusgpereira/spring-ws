package com.springws.app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springws.app.entities.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>{
	
	public UserEntity findByEmail(String email);
	public UserEntity findByUserId(String userId);
	public UserEntity findByEmailVerificationToken(String token);
}
