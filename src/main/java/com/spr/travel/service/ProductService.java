package com.spr.travel.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spr.travel.domain.Product;
import com.spr.travel.domain.Reservation;
import com.spr.travel.domain.SearchForm;
import com.spr.travel.repository.ProductRepository;
import com.spr.travel.repository.ReserRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository ;

	@Autowired
	private ReserRepository reserRepository ;

	
	public List<Product> getProductList(){ // 상품 테이블에 있는 모든 정보 찾기
		return productRepository.findAll();
	}


	public List<Product> productSearch(SearchForm search)
//	public List<Product> productSearch(String country, String departure, String plan, String seat, String city)
	{
//		return productRepository.findAll(search);
//		return productRepository.findOne(search);
/*		productRepository.findByProCountry(country),
		productRepository.findByProDeparture(departure),
		productRepository.findByProPlan(plan),
		productRepository.findByProSeat(seat),
		productRepository.findByProCity(city);*/
		return null;
	}

	public List<Product> getListBySearch(String country, String departure, String plan, String seat, String city)
	{return productRepository.findAll();}

	public int updateViewcnt(HttpServletRequest req, HttpServletResponse res, int id, Product product) {
		productRepository.save(product);
		return 0;
	}

	public List<Product> getListByContinent(String continent) { // DB에 등록된 대륙별 목록 출력
		return productRepository.findByProContinent(continent);
	}

	public List<Product> getListByCity(String city){// DB에 등록된 도시별 목록 출력
		return productRepository.findByProCity(city);
	}

	public Product getProductById(int id){ // DB에 등록된 상품 id로 상품 찾아서 가져오기
		return productRepository.findById(id).get();
	};

	public void saveListByNew(Product product){ // new페이지에서 등록된 상품 데이터 가져오기
		productRepository.save(product);
	}


	public int deleteProduct(int id){ // DB에 등록된 상품 id를 이용하여 상품 삭제하기
			productRepository.deleteById(id);
		return 1;
	}


	public int reserve(Reservation rvo) {
		// TODO Auto-generated method stub
		reserRepository.save(rvo);
		return 0;
	}

}
