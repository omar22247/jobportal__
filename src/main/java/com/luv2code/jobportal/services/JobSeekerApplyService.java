package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.JobSeekerApply;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;
@Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }
    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile jobSeeckerProfile) {
        return jobSeekerApplyRepository.findByUserId(jobSeeckerProfile);
    }
    public List<JobSeekerApply> getCandidates(JobPostActivity jobPostActivity) {
        return jobSeekerApplyRepository.findByJob(jobPostActivity);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
     jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
