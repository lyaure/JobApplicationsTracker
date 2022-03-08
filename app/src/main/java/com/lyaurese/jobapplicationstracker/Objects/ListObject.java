package com.lyaurese.jobapplicationstracker.Objects;

public class ListObject {
    private String name;
    private int type;
    private int numOfApplications;

    public ListObject(int type, String name, int numOfApplications){
        this.type = type;
        this.name = name;
        this.numOfApplications = numOfApplications;
    }

    public int getType() {
        return type;
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
