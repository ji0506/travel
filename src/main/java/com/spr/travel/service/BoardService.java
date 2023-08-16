package com.spr.travel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spr.travel.domain.Board;
import com.spr.travel.domain.Notice;
import com.spr.travel.repository.BoardRepository;
import com.spr.travel.repository.NoticeRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository BoardRepository;

	@Autowired
	private NoticeRepository noticeRepository;

	
	// 게시판 전체 리스트 조회
	public List<Board> getBoardList(){
		return BoardRepository.findAll();
	}

	// 게시글 작성
	public void Write(Board qvo) throws Exception {
		BoardRepository.save(qvo);
	}

	// 특정 카테고리의 게시글 리스트 조회
	public List<Board> getBoardCateList(int cateNo){
		return BoardRepository.findByCateNo(cateNo);
	}
	
	
	public List<Notice> getNoticeList(){
		return noticeRepository.findAll();
	}

	
	public List<Board> getNoticeCateList(int cateNo){
		return noticeRepository.findByCateNo(cateNo);
	}
	
	
	public void Write(Notice noti) throws Exception {
		noticeRepository.save(noti);
	}

	
	public Notice getByNoticeId(int id) throws Exception {
		return noticeRepository.findById(id).get();
	}

	public Board getByBoardId(int id) throws Exception {
		return BoardRepository.findById(id).get();
	}

	

}
