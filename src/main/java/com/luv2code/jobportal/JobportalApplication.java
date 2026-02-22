package com.luv2code.jobportal;

import com.luv2code.jobportal.repository.UserRepository;
import com.luv2code.jobportal.services.UserTypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static java.text.Normalizer.normalize;

@SpringBootApplication
public class JobportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobportalApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner( UserTypeService userTypeService) {
		return runner -> {
			displayAllUserTypes( userTypeService);
		};
	}
	public void displayAllUserTypes(UserTypeService userTypeService) {
//

		String s="photos/../photos/../../resume.pdf\n";
		String s1="../../../etc/passwd\n";
		System.out.println(StringUtils.cleanPath(s));
		System.out.println(StringUtils.cleanPath(s1));
	}
}
