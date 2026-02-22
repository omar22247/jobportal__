package com.luv2code.jobportal.controller;


import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.entity.UsersType;
import com.luv2code.jobportal.services.UserService;
import com.luv2code.jobportal.services.UserTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private final UserTypeService userTypeService;
    private final UserService userService;
    private static int x=1;
    @Autowired
    public UserController(UserTypeService userTypeService, UserService userService) {
         this.userService = userService;
        this.userTypeService = userTypeService;
     }
     @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        List<UsersType> userTypes = userTypeService.getAllUserTypes();
        model.addAttribute("getAllTypes", userTypes);
        model.addAttribute("user",new Users());
         return "register";
     }
     @PostMapping("/register/new")
    public String registerNewUser(@Valid Users user,Model model) {
         Optional<Users> optionalUser = userService.findByEmail(user.getEmail());
         if (optionalUser.isPresent()) {
             model.addAttribute("error", "Email already exists");
             List<UsersType> userTypes = userTypeService.getAllUserTypes();
             model.addAttribute("getAllTypes", userTypes);
             return "register";
            }
         userService.addUser(user);
         return "redirect:/dashboard/";
     }
     @GetMapping("/login")
    public String showLoginForm(HttpServletResponse response,HttpServletRequest request) {
         response.setHeader("test","omar");
         response.addIntHeader("id",x++);
        request.getHeader("Host");
        return "login";
     }
     @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         if (auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
         }
         return "redirect:/";
     }
}
