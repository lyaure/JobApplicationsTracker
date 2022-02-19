package com.lyaurese.jobapplicationstracker.Objects;

public class ListObject {
    private String name;
    private int numOfApplications;

    public ListObject(String name, int numOfApplications){
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
