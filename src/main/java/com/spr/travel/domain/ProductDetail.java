package com.spr.travel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="product_detail")
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length=11)
	private int detailNo;

	@Column(length=11)
	private int proNo;

	@Column(length=100)
	private String detailInfo;

	@Column(length=255)
	private String detailImage;
	
	@Column(length=4)
	private int detailViewcnt;

	@Column
	private String schedule;
}
