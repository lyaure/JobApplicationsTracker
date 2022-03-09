package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.CustomViews.GraphView;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.Objects.GraphEntry;
import com.lyaurese.jobapplicationstracker.R;
import com.lyaurese.jobapplicationstracker.Utils.GraphUtil;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    private PieChart sumPieChart, interviewsPieChart;
    private GraphView applicationsGraphView, companiesGraphView;
    private final String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Database db;
    private int[] colors;


    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        MainBoardActivity activity = (MainBoardActivity)getActivity();
        activity.setFragmentID(R.layout.fragment_board);

        db = new Database(getContext());

        ScrollView VSV = (ScrollView) view.findViewById(R.id.mainScrollView_ID);
        VSV.setVerticalScrollBarEnabled(false);

        sumPieChart = (PieChart)view.findViewById(R.id.sumPieChart_ID);
        interviewsPieChart = (PieChart)view.findViewById(R.id.interviewsPieChart_ID);
        applicationsGraphView = (GraphView)view.findViewById(R.id.applicationsGraphView_ID);
        companiesGraphView = (GraphView)view.findViewById(R.id.companiesGraphView_ID);

        HorizontalScrollView applicationsHSV = (HorizontalScrollView) view.findViewById(R.id.applicationsGraphViewHSV_ID);
        applicationsHSV.setHorizontalScrollBarEnabled(false);

        HorizontalScrollView companiesHSV = (HorizontalScrollView) view.findViewById(R.id.companiesGraphViewHSV_ID);
        companiesHSV.setHorizontalScrollBarEnabled(false);

        DisplayMetrics dm = new DisplayMetrics();

        // indicates to the graph the scree size
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels, height = dm.heightPixels;

        GraphEntry[] applicationsEntries = GraphUtil.getInitializedArray(db.getApplicationsByMonth());

        applicationsGraphView.setScreenDimensions(width, height);
        if(applicationsEntries != null)
            applicationsGraphView.setMax(GraphUtil.getMax(applicationsEntries));

        colors = new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.green), getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)};

        applicationsGraphView.setBarsColors(colors);
        applicationsGraphView.setEntries(applicationsEntries);

        applicationsGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    float x = event.getX();
                    float y = event.getY();

                    String label = applicationsGraphView.getObjectLabelAt(x, y);

                    if(!label.equals("null")){
                        changeFragment(2, label);
                    }
                }
                return true;
            }
        });


        applicationsHSV.post(new Runnable() {
            @Override
            public void run() {
                applicationsHSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        GraphEntry[] companiesEntries = GraphUtil.getInitializedArray(db.getApplicationsByCompany());

        companiesGraphView.setScreenDimensions(width, height);
        if(companiesEntries != null)
            companiesGraphView.setMax(GraphUtil.getMax(companiesEntries));
        companiesGraphView.setBarsColors(colors);
        companiesGraphView.setEntries(companiesEntries);

        companiesGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    float x = event.getX();
                    float y = event.getY();

                    String label = companiesGraphView.getObjectLabelAt(x, y);

                    if(!label.equals("null")){
                        changeFragment(0, label);
                    }
                }
                return true;
            }
        });

//        applicationsHSV.post(new Runnable() {
//            @Override
//            public void run() {
//                companiesHSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        });

        drawSummaryPieChart();
        drawInterviewsPieChart();

        return view;
    }

    private void drawSummaryPieChart(){
        List<PieEntry> entries = new ArrayList<>();

        int appSum = db.getApplicationsCount();
        int active = db.getActiveCount();
        int inactive = appSum - active;

        entries.add(new PieEntry((float)active, "Active"));
        entries.add(new PieEntry((float)inactive, "Inactive"));

        PieDataSet set = new PieDataSet(entries, " ");
//        GraphUtil.shuffleColors(colors);
        set.setColors(new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.green)});
        set.setValueTextSize(16f);

        PieData data = new PieData(set);

        sumPieChart.setData(data);
        sumPieChart.getDescription().setEnabled(false);
        sumPieChart.setDrawEntryLabels(false);
        sumPieChart.getLegend().setTextColor(getResources().getColor(R.color.lightGray));
        sumPieChart.setCenterText(appSum + "\nApplications");
        sumPieChart.setCenterTextSize(18f);
        sumPieChart.setCenterTextColor(getResources().getColor(R.color.lightGray));
        sumPieChart.setHoleColor(getResources().getColor(R.color.dark_grey));
        sumPieChart.animate();
    }

    private void drawInterviewsPieChart(){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float)db.getApplicationsCount(), "Applications"));
        entries.add(new PieEntry((float)db.getInterviewApplicationsCount(), "Interviews"));

        PieDataSet set = new PieDataSet(entries, " ");
//        GraphUtil.shuffleColors(colors);
        set.setColors(new int[]{getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)});
        set.setValueTextSize(16f);

        PieData data = new PieData(set);

        interviewsPieChart.setData(data);
        interviewsPieChart.getDescription().setEnabled(false);
        interviewsPieChart.setDrawEntryLabels(false);
        interviewsPieChart.getLegend().setTextColor(getResources().getColor(R.color.lightGray));
        interviewsPieChart.setCenterText(db.getInterviewApplicationsCount() + "\nInterviews");
        interviewsPieChart.setCenterTextSize(18f);
        interviewsPieChart.setCenterTextColor(getResources().getColor(R.color.lightGray));
        interviewsPieChart.setHoleColor(getResources().getColor(R.color.dark_grey));
        interviewsPieChart.animate();
    }

    private void changeFragment(int type, String label){
        Fragment fragment = new ApplicationPagerFragment();

        MainBoardActivity activity = (MainBoardActivity)getActivity();
        activity.setFragmentID(R.layout.fragment_application_pager);

        Bundle bundle = new Bundle();
        bundle.putInt("filter", -1);
        bundle.putInt("type", type);
        bundle.putString("name", label);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}