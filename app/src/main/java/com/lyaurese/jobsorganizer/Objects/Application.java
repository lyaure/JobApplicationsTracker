package com.lyaurese.jobsorganizer.Objects;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Application {
    private String jobTitle;
    private String jobNumber;
    private String companyName;
    private boolean applied;
    private Calendar calendar;
    private String comment;

    public Application(String jobTitle, String jobNumber, String companyName, boolean applied, Calendar calendar, String comment){
        this.jobTitle = jobTitle;
        this.jobNumber = jobNumber;
        this.companyName = companyName;
        this.applied = applied;
        this.calendar = calendar;
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

    public Calendar getCalendar() {
        return calendar;
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

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
