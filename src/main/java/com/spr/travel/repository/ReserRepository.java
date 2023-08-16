package com.spr.travel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spr.travel.domain.Reservation;


public interface ReserRepository extends JpaRepository<Reservation, Integer>{

    
    @Query(nativeQuery = true, value = "select * from reservation where user_id=:userID and pro_no=:proNo")
	public Reservation findByProNoAndUserId(String userID, int proNo);

	public Reservation findByProNo(int userID);

	public List<Reservation> findByUserId(String userId);

	
}
