package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private GraphView lastSevenDaysGraphView, applicationsByMonthsGraphView, companiesGraphView;
    private final String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Database db;
    private SharedPreferences sp;
    private TextView chartTitle1, chartTitle2;
    private Button swapCharts1;
    private final int COMPANY = 0, MONTHS = 4, LAST_SEVEN_DAYS = 3, TAGS = 2;
    private HorizontalScrollView applicationsHSV;
    private Button swapCharts2;
    private GraphView tagsGraphView;
    private HorizontalScrollView categoriesHSV;


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

        MainBoardActivity activity = (MainBoardActivity) getActivity();
        activity.setFragmentID(R.layout.fragment_board);

        db = Database.getInstance(getActivity());

        sp = activity.getSharedPreferences("applicationsChart", Context.MODE_PRIVATE);

        ScrollView VSV = (ScrollView) view.findViewById(R.id.mainScrollView_ID);
        VSV.setVerticalScrollBarEnabled(false);

        sumPieChart = (PieChart) view.findViewById(R.id.sumPieChart_ID);
        interviewsPieChart = (PieChart) view.findViewById(R.id.interviewsPieChart_ID);
        chartTitle1 = (TextView) view.findViewById(R.id.titleChart1Txtv_ID);
        chartTitle2 = (TextView) view.findViewById(R.id.titleChart2Txtv_ID);
        lastSevenDaysGraphView = (GraphView) view.findViewById(R.id.lastSevenDaysApplicationsGraphView_ID);
        applicationsByMonthsGraphView = (GraphView) view.findViewById(R.id.applicationsByMonthsGraphView_ID);
        companiesGraphView = (GraphView) view.findViewById(R.id.companiesGraphView_ID);
        tagsGraphView = (GraphView) view.findViewById(R.id.tagsGraphView_ID);

        swapCharts1 = (Button) view.findViewById(R.id.swapChartBtn1_ID);
        swapCharts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chartViewBy = sp.getInt("applicationsChart", LAST_SEVEN_DAYS);
                SharedPreferences.Editor editor = sp.edit();

                if (chartViewBy == LAST_SEVEN_DAYS)
                    editor.putInt("applicationsChart", MONTHS);
                else
                    editor.putInt("applicationsChart", LAST_SEVEN_DAYS);


                editor.commit();
                scrollViewRight(applicationsHSV);
                updateApplicationsChartViews(1);
            }
        });

        swapCharts2 = (Button) view.findViewById(R.id.swapChartBtn2_ID);
        swapCharts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chartViewBy = sp.getInt("categoryChart", TAGS);
                SharedPreferences.Editor editor = sp.edit();

                if (chartViewBy == TAGS) {
                    editor.putInt("categoryChart", COMPANY);

                } else {
                    editor.putInt("categoryChart", TAGS);

                }

                editor.commit();

                updateApplicationsChartViews(2);
                scrollViewLeft(categoriesHSV);
            }
        });

        updateApplicationsChartViews(1);
        updateApplicationsChartViews(2);

        applicationsHSV = (HorizontalScrollView) view.findViewById(R.id.applicationsGraphViewHSV_ID);
        applicationsHSV.setHorizontalScrollBarEnabled(false);

        categoriesHSV = (HorizontalScrollView) view.findViewById(R.id.categoriesGraphViewHSV_ID);
        categoriesHSV.setHorizontalScrollBarEnabled(false);

        DisplayMetrics dm = new DisplayMetrics();

        // indicates to the graph the scree size
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels, height = dm.heightPixels;
        int[] colors = new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.green), getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)};

        GraphEntry[] applicationsByMonthsEntries = GraphUtil.getInitializedArray(db.getApplicationsCountByMonth());
        setupGraphView(applicationsByMonthsGraphView, width, height, applicationsByMonthsEntries, colors);

        applicationsByMonthsGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX();
                    float y = event.getY();

                    String label = applicationsByMonthsGraphView.getObjectLabelAt(x, y);

                    if (!label.equals("null")) {
                        changeFragment(MONTHS, label);
                    }
                }
                return true;
            }
        });

        GraphEntry[] lastSevenDaysEntries = GraphUtil.getInitializedArray(db.getLastSevenDaysApplicationsCount());
        setupGraphView(lastSevenDaysGraphView, width, height, lastSevenDaysEntries, colors);

        lastSevenDaysGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX();
                    float y = event.getY();

                    String label = lastSevenDaysGraphView.getObjectLabelAt(x, y);

                    if (!label.isEmpty()) {
                        changeFragment(LAST_SEVEN_DAYS, label);
                    }
                }
                return true;
            }
        });

        scrollViewRight(applicationsHSV);

        GraphEntry[] companiesEntries = GraphUtil.getInitializedArray(db.getApplicationsCountByCompany());

        setupGraphView(companiesGraphView, width, height, companiesEntries, colors);

        companiesGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX();
                    float y = event.getY();

                    String label = companiesGraphView.getObjectLabelAt(x, y);

                    if (!label.isEmpty()) {
                        changeFragment(COMPANY, label);
                    }
                }
                return true;
            }
        });

        GraphEntry[] tagsEntry = GraphUtil.getInitializedArray(db.getApplicationsCountByTag());

        setupGraphView(tagsGraphView, width, height, tagsEntry, colors);

        tagsGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    float x = event.getX();
                    float y = event.getY();

                    String label = tagsGraphView.getObjectLabelAt(x, y);

                    if(!label.isEmpty()){
                        changeFragment(TAGS, label);
                    }
                }
                return true;
            }
        });

        drawSummaryPieChart();
        drawInterviewsPieChart();

        return view;
    }

    private void setupGraphView(GraphView graphView, int width, int height, GraphEntry[] entries, int[] colors) {
        graphView.setScreenDimensions(width, height);
        if (entries != null)
            graphView.setMax(GraphUtil.getMax(entries));

        graphView.setBarsColors(colors);
        graphView.setEntries(entries);
    }

    private void scrollViewRight(HorizontalScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    private void scrollViewLeft(HorizontalScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            }
        });
    }

    private void updateApplicationsChartViews(int chartNum) {
        if (chartNum == 1) {
            int chartViewBy = sp.getInt("applicationsChart", LAST_SEVEN_DAYS);

            if (chartViewBy == LAST_SEVEN_DAYS) {
                applicationsByMonthsGraphView.setVisibility(View.GONE);
                lastSevenDaysGraphView.setVisibility(View.VISIBLE);
                swapCharts1.setText("Months");
                chartTitle1.setText("Last 7 days applications");


            } else {
                applicationsByMonthsGraphView.setVisibility(View.VISIBLE);
                lastSevenDaysGraphView.setVisibility(View.GONE);
                swapCharts1.setText("Days");
                chartTitle1.setText("Last applications by month");
            }
        }
        else if(chartNum == 2){
            int chartViewBy = sp.getInt("categoryChart", LAST_SEVEN_DAYS);

            if (chartViewBy == TAGS) {
                tagsGraphView.setVisibility(View.VISIBLE);
                companiesGraphView.setVisibility(View.GONE);
                swapCharts2.setText("Companies");
                chartTitle2.setText("Applications by tags");
            } else {
                tagsGraphView.setVisibility(View.GONE);
                companiesGraphView.setVisibility(View.VISIBLE);
                swapCharts2.setText("Tags");
                chartTitle2.setText("Applications by companies");
            }
        }
    }

    private void drawSummaryPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        int appSum = db.getApplicationsCount();
        int active = db.getActiveCount();
        int inactive = appSum - active;

        entries.add(new PieEntry((float) active, "Active"));
        entries.add(new PieEntry((float) inactive, "Inactive"));

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

    private void drawInterviewsPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) db.getApplicationsCount(), "Applications"));
        entries.add(new PieEntry((float) db.getInterviewApplicationsCount(), "Interviews"));

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

    private void changeFragment(int type, String label) {
        Fragment fragment = new ApplicationPagerFragment();

        MainBoardActivity activity = (MainBoardActivity) getActivity();
        activity.setFragmentID(R.layout.fragment_application_pager);

        Bundle bundle = new Bundle();
        bundle.putInt("filter", -1);
        bundle.putInt("type", type);
        bundle.putString("name", label);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_ID, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}