package com.spr.travel.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name="product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length=11)
	private int proNo;
	
	@Column(length=20)
	private String proContinent;

	@Column(length=20)
	private String proCountry;

	@Column(length=20)
	private String proCity;

	@Column(length=50)
	private String proName;

	@Column(length=10)
	private String proAirplane;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date proDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	private Date proArrive;
	
	@Column(length=4)
	private int proPlan;
	
	@Column(length=4)
	private int proAdult;

	@Column(length=4)
	private int proMinor;
	
	@Column(length=4)
	private int proSeat;
	
	@OneToOne
	@JoinColumn(name = "proNo", insertable = false, updatable = false)
	@JsonIgnore
	private ProductDetail detail;
}
