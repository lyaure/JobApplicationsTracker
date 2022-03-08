package com.lyaurese.jobapplicationstracker.Objects;

import java.io.Serializable;
import java.util.Calendar;

public class Application implements Serializable {
    private int id;
    private String jobPosition;
    private String jobNumber;
    private String companyName;
    private boolean applied, interviewed;
    private Calendar appliedDate, interviewedDate;
    private String comment, location;
    private boolean active;

    public Application(int id, String companyName, String jobTitle, String jobNumber, String location, Calendar appliedDate, String comment){
        this.id = id;
        this.jobPosition = jobTitle;
        this.jobNumber = jobNumber;
        this.companyName = companyName;
        this.applied = true;
        this.interviewed = false;
        this.appliedDate = appliedDate;
        this.interviewedDate = null;
        this.comment = comment;
        this.active = true;
        this.location = location;
    }

    public int getId(){
        return this.id;
    }

    public String getJobPosition(){
        return this.jobPosition;
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

    public String getLocation(){
        return this.location;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
