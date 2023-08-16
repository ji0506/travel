package com.spr.travel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spr.travel.domain.Qna;
import com.spr.travel.repository.QnaRepository;

@Service
public class QnaService {
	@Autowired
    private QnaRepository QnaRepository;

    //게시판 리스트 조회
    public List<Qna> getBoardList(){
        return QnaRepository.findAll();
    }

    //게시글 작성
    public void Write(Qna qvo) throws Exception {
        QnaRepository.save(qvo);
    }

    //카테고리별 게시판 리스트 조회
    //public List<Qna>  getBoardCateList(int cateNo) throws Exception {
        //return BoardRepository.findByCateNo(cateNo);
    //}
}
