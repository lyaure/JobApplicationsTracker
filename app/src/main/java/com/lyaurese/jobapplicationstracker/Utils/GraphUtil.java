package com.lyaurese.jobapplicationstracker.Utils;

import com.lyaurese.jobapplicationstracker.Objects.GraphEntry;

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

//    public static void shuffleColors(int[] colors){
//        int n = colors.length;
//        int rounds = (int)(Math.random()*10);
//
//        for(int i=0; i<rounds; i++){
//            int index1 = 0, index2 = 0;
//
//            while(index1 == index2){
//                index1 = (int)(Math.random()*n);
//                index2 = (int)(Math.random()*n);
//
////                Toast.makeText(context, ""+ index1 + "  " + index2, Toast.LENGTH_SHORT).show();
//            }
//
//            swap(colors, index1, index2);
//        }
//
////        int n = colors.length;
////        int randNum = (int)(Math.random()*10);
////        Random random = new Random(System.currentTimeMillis() * randNum);
////        random.nextInt();
////
////        for(int i=0; i<n; i++){
////            int change = i + random.nextInt(n-i);
////            swap(colors, i, change);
////        }
//    }
//
//    private static void swap(int[] arr, int a, int b){
//        int temp = arr[a];
//        arr[a] = arr[b];
//        arr[b] = temp;
//    }


}
