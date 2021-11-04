package com.lyaurese.jobsorganizer.Objects;

public class Company {
    private String name;
    private int numOfApplications;

    public Company(String name, int numOfApplications){
        this.name = name;
        this.numOfApplications = numOfApplications;
    }

    public String getName() {
        return this.name;
    }

    public int getNumOfApplications(){
        return this.numOfApplications;
    }

    public void setNumOfApplications(int numOfApplications) {
        this.numOfApplications = numOfApplications;
    }
}
