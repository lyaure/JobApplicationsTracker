package com.lyaurese.jobsorganizer.Utils;

import com.lyaurese.jobsorganizer.Objects.GraphEntry;

public abstract class GraphUtil {
    private static final String[] MONTHS_NAMES = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final int MONTHS_NUMBERS = 12;

    public static GraphEntry[] getInitializedArrayByMonths() {
        GraphEntry[] entries = new GraphEntry[12];
        for (int i = 0; i < 12; i++) {
            entries[i] = new GraphEntry(0, MONTHS_NAMES[i]);
        }
        return entries;
    }

    public static GraphEntry[] getInitializedArrayByCompanies(int size, String[] labels) {
        GraphEntry[] entries = new GraphEntry[size];
        for (int i = 0; i < size; i++) {
            entries[i] = new GraphEntry(0, labels[i]);
        }
        return entries;
    }

    public static int getMax(GraphEntry[] entries) {
        int max = 0;
        for (GraphEntry entry : entries) {
            if (entry.getData() > max) {
                max = entry.getData();
            }
        }
        return max;
    }


}
