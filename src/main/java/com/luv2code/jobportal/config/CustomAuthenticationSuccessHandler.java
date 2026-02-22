package com.luv2code.jobportal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      UserDetails user= (UserDetails) authentication.getPrincipal();
      String username= user.getUsername();
        boolean hasJobSeekerRole =
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("Job Seeker"));
        boolean hasRecruiterRole=   authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("Recruiter"));
        if (hasJobSeekerRole || hasRecruiterRole) {
            response.sendRedirect("/dashboard/");
        }

    }
}
