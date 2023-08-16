package com.spr.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import com.spr.travel.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer>{

    /*
    메인 화면에 보여줄 글을 가져오는 쿼리
    메인 화면에서 최신 글 2개와 그 이외의 글 중 상위 3개 총 5개의 글이 정렬된 상태로 가져옴
    */
    @Query(nativeQuery = true, value = "select t1.* from (" +
            "select a.* from board a where kate_no = 1  ORDER BY reg_date desc LIMIT 2) t1 union select t2.* from ( " +
            "select b.* from board b where kate_no != 1  ORDER BY reg_date desc LIMIT 3) t2 ")
	public List<Board> findMainView();

    //카테고리 번호로 글을 가져옴
    public List<Board> findByCateNo(int cateNo);

}
