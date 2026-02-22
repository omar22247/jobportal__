package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.UsersType;
import com.luv2code.jobportal.entity.UsersType;
import com.luv2code.jobportal.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {
    private final UserTypeRepository userTypeRepository;
    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }
    public List<UsersType> getAllUserTypes() {
        return userTypeRepository.findAll();
    }
}
