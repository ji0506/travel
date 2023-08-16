package com.spr.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spr.travel.domain.Qna;

//Qna 엔티티 레파지토리
public interface QnaRepository extends JpaRepository<Qna, Integer>{

}
