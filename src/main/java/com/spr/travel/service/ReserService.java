package com.spr.travel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spr.travel.domain.Reservation;
import com.spr.travel.domain.User;
import com.spr.travel.repository.ReserRepository;

@Service
public class ReserService {
	
	
	@Autowired
	private ReserRepository reserRepository;
	
	public List<Reservation> getReserList(User user){
		return reserRepository.findByUserId(user.getUserId());
	}
	
    public Reservation getReservationOfMember(String userId, int proNo){
        return reserRepository.findByProNo(proNo);
    };


    
    public Reservation getReservationOfMember(int proNo){
        return reserRepository.findByProNo(proNo);
    };

}
