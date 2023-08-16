package com.spr.travel.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@NoArgsConstructor
@Entity
@Data
@Table(name ="user")
@DynamicInsert
public class User {

	@Id
	@Column(length=11)
	private String userId; //아이디

	@Column(length=100)
	private String userPwd; //비밀번호

	@Column(length=10)
	private String userName; //이름

	@Column(length=100)
	private String userGender; //성별

	@Column(length=15)
	private String userBirth; //생년월일

	@Column(length=20)
	private String userEmail; //이메일

	@Column(length=20)
	private String userCp; //핸드폰번호

	@Column(length=50)
	private String userAddr; //주소

	@Column(length=50)
	private String userDaddr; //상세주소

	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	private String userGrade;

	private String userTypeCd;

	@Column(length=2)
	private String authId;
	
	@OneToOne
	@JoinColumn(name = "authId", insertable = false, updatable = false)
	@JsonIgnore
	private UserAuth userAuth;

}

