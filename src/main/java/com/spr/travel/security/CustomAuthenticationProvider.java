package com.spr.travel.security;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spr.travel.Member.UserService;
import com.spr.travel.domain.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService service;
		
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userID = (String) authentication.getPrincipal(); // 입력한 아이디 받음
		String password = (String) authentication.getCredentials(); // 입력한 비밀번호 받음
		
		String hashedPassword = sha256Hash(password);// 스프링 시큐리티를 통해 암하화해도됨. 함호화 방식은 sha256. 그 외에 암호화 방식으로는 sha1, base64등이 존재.
		
		hashedPassword  = hashedPassword.toUpperCase(); // 전부 다 대문자로
		User user = service.getLoginUser(userID); // userid로 검색 정보 가져오기

		if (user == null) { // user 확인
			throw new UsernameNotFoundException("아이디 또는 패스워드가 일치하지 않습니다.");
		}
		
		String errMsg = "정상적으로 로그인 되었습니다.";
		
		if(!user.getUserPwd().equals(hashedPassword)){ //비밀번호 일치할 시 로그인
			errMsg = "아이디 또는 패스워드가 일치하지 않습니다.";
			throw new BadCredentialsException(errMsg);
		}
	       
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(user.getUserAuth().getAuthName())); // 권한등록
        
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, password, roles); // token 생성
        result.setDetails(user);
		
        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	
	

	public static String sha256Hash(String input) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
	
	        // 해시 값을 16진수 문자열로 변환
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	
	        return hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
