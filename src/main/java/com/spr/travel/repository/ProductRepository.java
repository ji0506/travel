package com.spr.travel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spr.travel.domain.Product;


public interface ProductRepository extends JpaRepository<Product, Integer>{

    @Query(nativeQuery = true, value ="select * from product where pro_country = :country")
    public List<Product> findByProCountry(String country);
    @Query(nativeQuery = true, value ="select * from product where pro_departure = :departure")
    public List<Product> findByProDeparture(String departure);
    @Query(nativeQuery = true, value ="select * from product where pro_plan = :plan")
    public List<Product> findByProPlan(String plan);
    @Query(nativeQuery = true, value ="select * from product where pro_seat = :seat")
    public List<Product> findByProSeat(String seat);
    @Query(nativeQuery = true, value ="select * from product where pro_city = :city")
    public List<Product> findByProCity(String city);
    
    @Query(nativeQuery = true, value ="select max(pro_no) from product")
    public List<Product> findByPro(String city);

    public List<Product> findByProContinent(String continent);

//    public Product findByProNoconti(int id);

 //   @Query(nativeQuery = true, value ="select max(pro_no) from product")
//    public Product find(int id);
}
