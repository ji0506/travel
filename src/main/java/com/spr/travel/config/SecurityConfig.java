package com.spr.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spr.travel.auth.PrincipalDetailService;
import com.spr.travel.security.CustomAuthenticationProvider;
import com.spr.travel.security.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

//@EnableWebSecurity //시큐리티 필터가 등록
//@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

	private PrincipalDetailService userDetailsService;

	private String connectPath = "/upload/**";
	private String resourcePath = "file:///C:/upload/";

	public void configure(AuthenticationManagerBuilder auth) throws Exception {

//      String password = passwordEncoder().encode("1111");
//
//      auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
//      auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER");
//      auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
//      auth.userDetailsService(userDetailsService);
		// auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(connectPath).addResourceLocations(resourcePath); //외부이미지 경로
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeHttpRequests(authorize -> {
			try {
				authorize
						.antMatchers("/css/**", "/images/**", "/js/**", "/main/*.do", "/member/*.do",
								"/member/join.do", "/products/*.do")
						.permitAll() // 해당 경로는 인증 없이 접근 가능
						.antMatchers("/member/test.do","/board/qnaWrite.do") // 해당 경로는 인증이 필요
						.hasAnyRole("USER", "ADMIN") // ROLE 이 USER,ADMIN 가 포함된 경우에만 인증 가능
						.antMatchers("/products/new") // 해당 경로는 인증이 필요
						.hasRole("ADMIN") // ROLE 이 USER,ADMIN 가 포함된 경우에만 인증 가능
						.and()
							.formLogin().loginPage("/member/main.do") // 로그인 페이지 설정
							.loginProcessingUrl("/login-process.do") // 로그인 처리 URL 설정
							.usernameParameter("userId")
							.passwordParameter("userPwd")
							.defaultSuccessUrl("/main/main.do") // 로그인 성공 후 이동할 페이지
							.successHandler(new LoginSuccessHandler()) // 로그인 성공 후 처리할 핸들러
							.failureUrl("/member/main.do?fail=true") // 로그인 실패시 처리
							.permitAll()
						.and()
							.exceptionHandling()
							.accessDeniedPage("/main/main.do") // 권한 없을 때 보여주는 페이지
						.and()
							.logout()
							.logoutUrl("/member/logout.do") // 로그아웃 처리 URL 설정
							.logoutSuccessUrl("/member/main.do?logout=1") // 로그아웃 성공 후 이동할 페이지
							.deleteCookies("JSESSIONID"); // 로그아웃 후 쿠키 삭제
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		userDetailsService = new PrincipalDetailService();
		return userDetailsService;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(new CustomAuthenticationProvider());
	}

}