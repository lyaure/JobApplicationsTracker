package com.lyaurese.jobapplicationstracker.Objects;

public class Tag {
    private String name;
    private boolean checked;

    public Tag(String name, boolean checked){
        this.name = name;
        this.checked = checked;
    }

    public String getName(){
        return this.name;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
