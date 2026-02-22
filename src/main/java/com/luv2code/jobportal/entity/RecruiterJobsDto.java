package com.luv2code.jobportal.entity;

public class RecruiterJobsDto {
    private long totalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private JobLocation jobLocationId;
    private JobCompany jobCompanyId;

    public RecruiterJobsDto() {
    }

    public RecruiterJobsDto(long totalCandidates, Integer jobPostId, String jobTitle, JobLocation jobLocation, JobCompany jobCompany) {
        this.totalCandidates = totalCandidates;
        this.jobPostId = jobPostId;
        this.jobTitle = jobTitle;
        this.jobLocationId = jobLocation;
        this.jobCompanyId = jobCompany;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(long totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationID(JobLocation jobLocation) {
        this.jobLocationId = jobLocation;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompany) {
        this.jobCompanyId = jobCompany;
    }

    @Override
    public String toString() {
        return "RecruiterJobsDto{" +
                "totalCandidates=" + totalCandidates +
                ", jobPostId=" + jobPostId +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobLocation=" + jobLocationId +
                ", jobCompany=" + jobCompanyId +
                '}';
    }
}
