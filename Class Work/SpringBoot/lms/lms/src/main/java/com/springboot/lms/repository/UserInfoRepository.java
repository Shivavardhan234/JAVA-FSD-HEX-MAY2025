package com.springboot.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.lms.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

	
	@Query("select u from UserInfo u where u.username=?1")
	UserInfo getByUsername(String username);

}
