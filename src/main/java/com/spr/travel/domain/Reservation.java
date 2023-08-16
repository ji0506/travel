package com.spr.travel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@DynamicInsert
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length=11)
	private int revNum;

	@Column(length=11)
	private int proNo;
	
	@Column(length=11)
	private String userId;
	
	@Column(length=50)
	private String revName;

	@Column(length=50)
	private String revPhone;

	@Column(length=50)
	private String revBirth;

	@Column(length=50)
	private String revEmail;

	

	@Column(length=11)
	private int revAdult;

	@Column(length=11)
	private int revMinor;

	@OneToOne
	@JoinColumn(name = "proNo", insertable = false, updatable = false)
	@JsonIgnore
	private Product product;

	
}
