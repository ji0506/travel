package com.spr.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spr.travel.domain.ProductDetail;


public interface DetailRepository extends JpaRepository<ProductDetail, Integer>{

    public ProductDetail findByProNo(int id); // ProNo로 상세 테이블에서 찾기

    public ProductDetail findById(int id); // ID로 상세 테이블에서 찾기


}
