package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.JobSeekerSave;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.services.JobPostActivityService;
import com.luv2code.jobportal.services.JobSeeckerProfileService;
import com.luv2code.jobportal.services.JobSeekerSaveService;
import com.luv2code.jobportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobSeeckerProfileService jobSeeckerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final UserService userService ;

    @Autowired
    public JobSeekerSaveController(JobSeekerSaveService jobSeekerSaveService, JobSeeckerProfileService jobSeeckerProfileService, JobPostActivityService jobPostActivityService, UserService userService) {
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobSeeckerProfileService = jobSeeckerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
    }
    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username=authentication.getName();
            Optional<Users> users =userService.findByEmail(username);
            Optional<JobSeekerProfile> seekerProfile=jobSeeckerProfileService.getOne(users.get().getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(seekerProfile.isPresent()&&jobPostActivity!=null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            }else{
                throw new RuntimeException("User not found");
            }
            jobSeekerSave.setId(null);
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";

    }
    @GetMapping("saved-jobs/")
    public String saveJobs(Model model){
        List<JobPostActivity> jobPostActivityList=new ArrayList<>();
        Object currentUserProfile=userService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
            jobPostActivityList.add(jobSeekerSave.getJob());
        }
    model.addAttribute("jobPost",jobPostActivityList);
    model.addAttribute("user",currentUserProfile);

        return "saved-jobs";
    }


}
