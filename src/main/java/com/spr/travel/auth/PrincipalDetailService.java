package com.spr.travel.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spr.travel.domain.User;
import com.spr.travel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService  {

	@Autowired
	private UserRepository userRepository;
	
	// 스프링이 로그인 요청을 가로챌때 username, password변수 2개를 가로채는데
	// password 부분 처리는 알아서처리,
	// username이 DB에 있는지 확인해줘야함
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findById(username).orElseThrow(() -> {
			return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + username);
		});
		return new PrincipalDetail(principal); // 시큐리티의 세션에 유저정보가 저장이됨. (원래는 콘솔창에 뜨는 user, pw가 있었음)
	}
}
