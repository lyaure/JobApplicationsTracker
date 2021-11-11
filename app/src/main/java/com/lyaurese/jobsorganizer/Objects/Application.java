package com.lyaurese.jobsorganizer.Objects;

import java.io.Serializable;
import java.util.Calendar;

public class Application implements Serializable {
    private String jobTitle;
    private String jobNumber;
    private String companyName;
    private boolean applied, interviewed;
    private Calendar appliedDate, interviewedDate;
    private String comment;

    public Application(String companyName, String jobTitle, String jobNumber, boolean applied, Calendar appliedDate, String comment){
        this.jobTitle = jobTitle;
        this.jobNumber = jobNumber;
        this.companyName = companyName;
        this.applied = applied;
        this.interviewed = false;
        this.appliedDate = appliedDate;
        this.interviewedDate = null;
        this.comment = comment;
    }

    public String getJobTitle(){
        return this.jobTitle;
    }

    public String getJobNumber(){
        return this.jobNumber;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public boolean applied(){
        return applied;
    }

    public boolean interview() {
        return interviewed;
    }

    public Calendar getAppliedDate() {
        return appliedDate;
    }

    public Calendar getInterviewDate() {
        return interviewedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public void setInterview(boolean interviewed) {
        this.interviewed = interviewed;
    }

    public void setAppliedDate(Calendar appliedDate) {
        this.appliedDate = appliedDate;
        if(appliedDate != null)
            this.applied = true;
        else
            this.applied = false;
    }

    public void setInterviewDate(Calendar interviewedDate) {
        this.interviewedDate = interviewedDate;
        if(interviewedDate != null)
            this.interviewed = true;
        else
            this.interviewed = false;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
