package com.luv2code.jobportal.controller;

import ch.qos.logback.core.util.StringUtil;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UserRepository;
import com.luv2code.jobportal.services.RecruiterProfileService;
import com.luv2code.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile/")
public class RecruiterProfileController {
    private final UserRepository  userRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UserRepository userRepository, RecruiterProfileService recruiterProfileService) {
        this.userRepository = userRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found" + username));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(user.getUserId());
             if(!recruiterProfile.isEmpty()) {
                    model.addAttribute("profile", recruiterProfile.get());
                System.out.println(recruiterProfile);
             }
        }
        return "recruiter_profile";
    }
    @PostMapping("/addNew")
    public String addRecruiterProfile(RecruiterProfile recruiterProfile,
                                      @RequestParam("image") MultipartFile image,Model model) {


        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken) ) {
            String username =auth.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found" + username));
            recruiterProfile.setUserId(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }
        model.addAttribute("profile", recruiterProfile);
        String fileNmae="";
        if(!(image.getOriginalFilename().equals(""))) {
            fileNmae = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileNmae);
        }
        RecruiterProfile savedProfile = recruiterProfileService.addNew(recruiterProfile);
        String uploadDir = "photos/recruiter" + savedProfile.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileNmae, image);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  "redirect:/dashboard/";
    }
}
