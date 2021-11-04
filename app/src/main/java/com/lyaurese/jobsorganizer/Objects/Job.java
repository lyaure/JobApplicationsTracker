package com.lyaurese.jobsorganizer.Objects;

public class Job {
    private String jobTitle;
    private String jobName;
    private String companyName;
    private boolean isCVSent;

    public Job(String jobTitle, String jobName, String companyName, boolean isCVSent){
        this.jobTitle = jobTitle;
        this.jobName = jobName;
        this.companyName = companyName;
        this.isCVSent = isCVSent;
    }

    public String getJobTitle(){
        return this.jobTitle;
    }

    public String getJobName(){
        return this.jobName;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public boolean isCVSent(){
        return isCVSent;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCVSent(boolean CVSent) {
        this.isCVSent = CVSent;
    }
}
