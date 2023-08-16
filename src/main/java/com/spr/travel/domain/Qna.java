package com.spr.travel.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the boards database table.
 * 
 */
@Entity
@Data
@DynamicInsert
public class Qna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length=11)
	private int qnaNo;

	@Column(length=40)
	private String qnaTitle;

	@Lob
	private String qnaContent;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	
	@Column(length=11)
	private String userId;

	@Column(length=5)
	private int cnt;

	//기본 생성자
	public Qna() {

	}

	//제목, 내용 생성자
	public Qna(String title, String content) {
		this.qnaTitle=title;
		this.qnaContent=content;
	}
}