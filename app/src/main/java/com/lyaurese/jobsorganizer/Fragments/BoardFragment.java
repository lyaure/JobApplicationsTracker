package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lyaurese.jobsorganizer.CustomViews.GraphView;
import com.lyaurese.jobsorganizer.Objects.Database;
import com.lyaurese.jobsorganizer.Objects.GraphEntry;
import com.lyaurese.jobsorganizer.R;
import com.lyaurese.jobsorganizer.Utils.GraphUtil;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    private PieChart pieChart;
    private GraphView applicationsGraphView, companiesGraphView;
    private final String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Database db;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        db = new Database(getContext());

        pieChart = (PieChart)view.findViewById(R.id.pieChart_ID);
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

        GraphEntry[] applicationsEntries = db.getApplicationsByMonth();

        applicationsGraphView.setScreenDimensions(width, height);
        applicationsGraphView.setMax(GraphUtil.getMax(applicationsEntries));
        applicationsGraphView.setEntries(applicationsEntries);

        applicationsHSV.post(new Runnable() {
            @Override
            public void run() {
                applicationsHSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        GraphEntry[] companiesEntries = db.getApplicationsByCompany();

        companiesGraphView.setScreenDimensions(width, height);
        companiesGraphView.setMax(GraphUtil.getMax(companiesEntries));
        companiesGraphView.setEntries(companiesEntries);

        applicationsHSV.post(new Runnable() {
            @Override
            public void run() {
                companiesHSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        drawSummaryPieChart();

        return view;
    }

    private void drawSummaryPieChart(){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float)db.getNoResponseApplications(), "No response"));
        entries.add(new PieEntry((float)db.getInterviewApplications(), "Interview"));

        PieDataSet set = new PieDataSet(entries, "Applications");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setValueTextSize(16f);

        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("My\napplications");
        pieChart.animate();
    }

}