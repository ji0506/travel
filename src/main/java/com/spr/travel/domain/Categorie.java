package com.spr.travel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Data;


@Entity
@Table(name="Categorie")
@NamedQuery(name="Categorie.findAll", query="SELECT c FROM Categorie c")
@Data
@DynamicInsert
public class Categorie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int CateNo;
	
	@Column(length=20)
	private String CateName;

	@Column(length=20)
	private String CateDetail;
	
	@Column(length=2)
	private String userTypeCd;
}