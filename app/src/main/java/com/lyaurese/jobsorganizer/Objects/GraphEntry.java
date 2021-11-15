package com.lyaurese.jobsorganizer.Objects;

import android.graphics.Point;

public class GraphEntry {
    private int data;
    private String label;
    private Point point = new Point();

    public GraphEntry(int data, String label) {
        this.data = data;
        this.label = label;
    }

    public int getData() {
        return this.data;
    }

    public String getLabel() {
        return this.label;
    }

    public Point getPoint() {
        return this.point;
    }

    public void incData(int count) {
        this.data += count;
    }

    public void setPoint(int x, int y) {
        this.point.set(x, y);
    }
}
