package com.lyaurese.jobapplicationstracker.Objects;

import java.util.ArrayList;

public class TagList {
    private ArrayList<Tag> list;

    public TagList(ArrayList<Tag> tags){
        this.list = tags;
    }

    public void setItemChecked(int position, boolean checked){

    }

    public ArrayList<Tag> getList() {
        return list;
    }
}
