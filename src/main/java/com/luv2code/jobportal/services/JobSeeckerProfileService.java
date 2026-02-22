package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeeckerProfileRepository;
import com.luv2code.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeeckerProfileService {
     private final JobSeeckerProfileRepository jobSeeckerProfileRepository;
     private final UserRepository userRepository;
     @Autowired
    public JobSeeckerProfileService(JobSeeckerProfileRepository jobSeeckerProfileRepository, UserRepository userRepository) {
        this.jobSeeckerProfileRepository = jobSeeckerProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer userId) {
       return jobSeeckerProfileRepository.findById(userId);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeeckerProfile) {
        return jobSeeckerProfileRepository.save(jobSeeckerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                String username=authentication.getName();
                Users user=  userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found"+username));
                Optional<JobSeekerProfile > jobSeekerProfile=getOne(user.getUserId());
                return jobSeekerProfile.orElse(null);
            }
            else return null;
        }

}
