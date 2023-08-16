package com.spr.travel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spr.travel.domain.Categorie;


public interface CateRepository extends JpaRepository<Categorie, Integer>{
	
	
	public List<Categorie> findByUserTypeCd(String userTypeNo);
	
}
