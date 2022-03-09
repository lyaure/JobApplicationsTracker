package com.lyaurese.jobapplicationstracker.Objects;

import java.io.Serializable;
import java.util.Calendar;

public class Application implements Serializable {
    private int id;
    private String jobPosition;
    private String jobNumber;
    private String companyName;
    private boolean applied, interviewed;
    private long appliedDate, interviewedDate;
    private String comment, location;
    private boolean active;

    public Application(int id, String companyName, String jobTitle, String jobNumber, String location, long timeInMillis, String comment){
        this.id = id;
        this.jobPosition = jobTitle;
        this.jobNumber = jobNumber;
        this.companyName = companyName;
        this.applied = true;
        this.interviewed = false;
        this.appliedDate = timeInMillis;
        this.interviewedDate = 0;
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

    public long getAppliedDate() {
        return appliedDate;
    }

    public long getInterviewDate() {
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

    public void setAppliedDate(long timeInMillis) {
        this.appliedDate = timeInMillis;
    }

    public void setInterviewDate(long timeInMillis) {
        this.interviewedDate = timeInMillis;
        if(timeInMillis != 0)
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
