package com.luv2code.jobportal.repository;

import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UsersType,Integer> {
}
