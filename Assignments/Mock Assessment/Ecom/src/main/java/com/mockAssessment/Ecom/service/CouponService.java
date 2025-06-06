package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Coupon;
import com.mockAssessment.Ecom.repository.CouponRepository;

@Service
public class CouponService {
	
	CouponRepository couponRepository;
	
	
	
	
	

	public CouponService(CouponRepository couponRepository) {
		super();
		this.couponRepository = couponRepository;
	}






	public Coupon getCouponbyId(int id) throws ResourceNotFoundException {
		
		return couponRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No Coupon with the given id...!!!"));
	}






	public List<Coupon> getAllCoupon() {
		
		return couponRepository.findAll();
	}






	public Coupon addCoupon(Coupon coupon) {
		
		return couponRepository.save(coupon);
	}






	public Coupon updateCoupon(Coupon coupon) {
		if(coupon.getCouponCode()!=null)
		
		return null;
	}

}
