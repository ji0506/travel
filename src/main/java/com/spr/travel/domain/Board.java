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
@Table(name="board")
@NamedQuery(name="Board.findAll", query="SELECT b FROM Board b")
@Data
@DynamicInsert
public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length=11)
	private int brdNo;

	@Column(length=200)
	private String title;

	@Lob
	private String content;
	
	private String fileName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;

	@Column(length=2)
	private int cateNo;
	
	@Column(length=2)
	private int voteNo;

	@Column(length=10)
	private int cnt;

	@Column(length=11)	
	private String userId;

	//기본 생성자
	public Board() {
	
	}
	//제목, 내용, 카테고리 내용 생성자
	public Board(String title, String content, int cateNo) {
		super();
		this.title = title;
		this.content = content;
		this.cateNo = cateNo;
	}

	//제목, 내용 생성자
	public Board(String title, String content) {
		this.title = title;
		this.content = content;
	}


}