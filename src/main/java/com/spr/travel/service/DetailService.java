package com.spr.travel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spr.travel.domain.ProductDetail;
import com.spr.travel.repository.DetailRepository;

@Service
public class DetailService {

	@Autowired
	private DetailRepository detailRepository;

	public List<ProductDetail> getProductDetailList(){ // 상세내용 전부 출력
		return detailRepository.findAll();
	}


	public int createProduct(ProductDetail detail){ // 새글 작성 시 상세 내용 저장
		try {
			detailRepository.save(detail);


		}catch (Exception e){
			return -1;
		}

		return 0;
	}
	public ProductDetail getDetailById(int id){ // 특정 페이지에서 그 페이지의 상세 내용 출력
		return detailRepository.findByProNo(id);
	};

	public ProductDetail updateDetailByProNo(int id) { // 수정을 위한 기능
		ProductDetail detail = detailRepository.findByProNo(id);
		detailRepository.save(detail); // 상세 테이블 수정
		return detail;
	}

	public void saveListByNew(ProductDetail detail){ // new 페이지에서 입력된 데이터 상세 테이블에 저장
		detailRepository.save(detail);
	}

	public void deleteFileById(int id){ // 삭제 기능
		ProductDetail detail = detailRepository.findById(id);
		detailRepository.delete(detail);
	}


	public void updateFilePath(ProductDetail detail) {
		detailRepository.save(detail);
	}

}
