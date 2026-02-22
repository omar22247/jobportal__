package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeeckerProfileRepository;
import com.luv2code.jobportal.repository.RecruiterProfileRepository;
import com.luv2code.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final  UserRepository userRepository;
    private final JobSeeckerProfileRepository JobSeeckerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, JobSeeckerProfileRepository JobSeeckerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
        this.userRepository = userRepository;
        this.JobSeeckerProfileRepository = JobSeeckerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Users addUser(Users user) {
        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        int userTypeId = user.getUserTypeId().getUserTypeId();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users Saveduser =userRepository.save(user);
        if(userTypeId == 1 ) {
            recruiterProfileRepository.save(new RecruiterProfile(user));
        } else if (userTypeId == 2) {
            JobSeeckerProfileRepository.save(new JobSeekerProfile(user));
        }
        return Saveduser;
    }
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication auth= SecurityContextHolder.getContext()
                .getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            Users userr= userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"+   username));
            int userId= userr.getUserId();
            if(auth.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile recruiterProfile= recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
                return recruiterProfile;
            } else {
                JobSeekerProfile jobSeeckerProfile= JobSeeckerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
                System.out.println(jobSeeckerProfile);
                return jobSeeckerProfile;
            }

        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
            return user;
        }
        return null;
    }
}
