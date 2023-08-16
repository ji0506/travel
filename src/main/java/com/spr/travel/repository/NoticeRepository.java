package com.spr.travel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spr.travel.domain.Board;
import com.spr.travel.domain.Notice;



public interface NoticeRepository extends JpaRepository<Notice, Integer>{

	
    public List<Board> findByCateNo(int cateNo);

}
