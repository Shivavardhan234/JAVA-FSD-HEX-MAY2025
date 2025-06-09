package com.mockAssessment.Ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Coupon;
import com.mockAssessment.Ecom.service.CouponService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
	
	@Autowired
	CouponService couponService;
	
	@PostMapping("/add")
	public Coupon addCoupon(@RequestBody Coupon coupon) {
		return couponService.addCoupon(coupon);
	}
	
	@GetMapping("/get/by-id/{id}")
	public Coupon getCouponById(@PathVariable int id) throws ResourceNotFoundException {
		return couponService.getCouponbyId(id);
	}

	@GetMapping("/get/all")
	public List<Coupon> getAllCoupon(){
		return couponService.getAllCoupon();
	}
	
	@PutMapping("/update")
	public Coupon updateCoupon(@RequestBody Coupon coupon) {
		return couponService.updateCoupon(coupon);
	}

}
