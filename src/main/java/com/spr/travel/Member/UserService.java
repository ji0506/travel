package com.spr.travel.Member;

import com.spr.travel.domain.Reservation;
import com.spr.travel.domain.User;
import com.spr.travel.repository.ReserRepository;
import com.spr.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  ReserRepository reserRepository;

    
    public User getLoginUser(String userId) {
    	try {
            return userRepository.findById(userId).get();
			
		} catch (Exception e) {
			e.fillInStackTrace();
			return null;
		}    	
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userId) {
    	try {
        	return userRepository.findById(userId).get();			
		} catch (Exception e) {
			return null;
		}


    }

    public User getUserByEmail(String userEmail) {
        try {
            return userRepository.findByUserEmail(userEmail).get();
        } catch (Exception e) {
            return null;
        }
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

	public List<User> memberList() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	public List<Reservation> allRev() {
		// TODO Auto-generated method stub
		return null;
	}


}