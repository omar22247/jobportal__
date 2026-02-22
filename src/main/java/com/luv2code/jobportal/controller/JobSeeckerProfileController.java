package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.Skills;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UserRepository;
import com.luv2code.jobportal.services.JobSeeckerProfileService;
import com.luv2code.jobportal.services.UserService;
import com.luv2code.jobportal.util.FileDownloadUtil;
import com.luv2code.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeeckerProfileController {
    private final UserService userService;
    private JobSeeckerProfileService jobSeeckerProfileService;
    private final UserRepository userRepository;
    @Autowired
    public JobSeeckerProfileController(UserRepository userRepository, JobSeeckerProfileService jobSeeckerProfileService, UserService userService) {
        this.userRepository = userRepository;
        this.jobSeeckerProfileService = jobSeeckerProfileService;
        this.userService = userService;
    }
    @GetMapping("/")
    public String displayJobSeeckerProfile(Model model) {
        JobSeekerProfile jobSeeckerProfile = new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = userRepository.findByEmail(authentication.getName()).orElseThrow( ()->
                    new UsernameNotFoundException("User not found with email: " + authentication.getName()));
            Optional<JobSeekerProfile> jobSeeckerProfile1 = jobSeeckerProfileService.getOne(user.getUserId());
            if(jobSeeckerProfile1.isPresent()) {
                jobSeeckerProfile = jobSeeckerProfile1.get();
                if(jobSeeckerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeeckerProfile.setSkills(skills);
                }
                model.addAttribute("profile", jobSeeckerProfile);
                model.addAttribute("skills", skills);
            }


        }
        return "job-seecker-profile";
    }
    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeeckerProfile, @RequestParam("image") MultipartFile image,@RequestParam("pdf") MultipartFile pdf , Model model) {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                Users user = userRepository.findByEmail(authentication.getName()).orElseThrow( ()->
                        new UsernameNotFoundException("User not found with email: " + authentication.getName()));
                jobSeeckerProfile.setUserId(user);
                jobSeeckerProfile.setUserAccountId(user.getUserId());
//                jobSeeckerProfile.setProfilePhoto();
//                jobSeeckerProfile.setResume();
            }
            List<Skills> skillsList = new ArrayList<>();
            model.addAttribute("profile", jobSeeckerProfile);
            model.addAttribute("skills", skillsList);
            for (Skills skill: jobSeeckerProfile.getSkills()) {
                skill.setJobSeekerProfile(jobSeeckerProfile);
            }
            String imageName="";
            String pdfName="";
            if(!image.getOriginalFilename().equals("")) {
                imageName = StringUtils.cleanPath(Objects.requireNonNull(
                        image.getOriginalFilename()
                ));
                jobSeeckerProfile.setProfilePhoto(imageName);
            }
            if(!pdf.getOriginalFilename().equals("")) {
                pdfName = StringUtils.cleanPath(Objects.requireNonNull(
                        pdf.getOriginalFilename()
                ));
                jobSeeckerProfile.setResume(pdfName);
            }
        JobSeekerProfile jobSeeckerProfile1=       jobSeeckerProfileService.addNew(jobSeeckerProfile);
        try {
            String uploadDir = "photos/candidate/" + jobSeeckerProfile1.getUserAccountId();
            if(!image.getOriginalFilename().equals("")) {
                FileUploadUtil.saveFile(uploadDir,imageName,image);
            }
            if(!pdf.getOriginalFilename().equals("")) {
                FileUploadUtil.saveFile(uploadDir,pdfName,pdf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard/";
    }
    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model){
        Optional<JobSeekerProfile> seekerProfile=jobSeeckerProfileService.getOne(id);
        model.addAttribute("profile",seekerProfile.get());
        return "job-seecker-profile";
    }
    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam("fileName") String fileName, @RequestParam("userID") int userId){
        FileDownloadUtil fileDownloadUtil= new FileDownloadUtil();
        Resource resource=null;
        try {
            System.out.println("photos/candidate/"+userId+"/"+fileName);
            resource=fileDownloadUtil.getFileAsResource("photos/candidate/"+userId,fileName);
        }catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
        if(resource==null) {
            return new  ResponseEntity<>("File اjgajgnot found",HttpStatus.NOT_FOUND);
        }
        String contentType="application/octet-stream";
        String headerValue="attachment; filename=\""+resource.getFilename()+"\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).
                header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}
