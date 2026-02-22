package com.luv2code.jobportal.config;

import com.luv2code.jobportal.services.CustomerUserDetailsService;
import com.luv2code.jobportal.util.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    private final CustomerUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    public WebSecurityConfig(CustomerUserDetailsService customUserDetailsService , CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    private final String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};
    @Bean
        protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authenticationProvider(authenticationProvider());
            http.authorizeHttpRequests(auth ->{
                    auth.requestMatchers(publicUrl).permitAll();
                    auth.anyRequest().authenticated();
            });
            http.formLogin(form -> form.loginPage("/login").permitAll()
                    .successHandler(customAuthenticationSuccessHandler)).logout(logout->
            {
                logout.logoutUrl("/logout");
                logout.logoutSuccessUrl("/");
            } ).cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable() );

            return http.build();
        }
    @Bean
    protected AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder( passwordEncoder());
        return  authProvider;
    }
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
