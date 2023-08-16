package com.spr.travel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spr.travel.domain.User;


public interface UserRepository extends JpaRepository<User, String>{
	
	public Optional<User> findByUserName(String username);


	public Optional<User> findByUserEmail(String email);


}
