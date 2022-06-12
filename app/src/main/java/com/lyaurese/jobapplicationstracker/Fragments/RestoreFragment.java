package com.lyaurese.jobapplicationstracker.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;
import java.util.List;

public class RestoreFragment extends Fragment {
    private String title;
    private PieChart sum, interview;
    private Database db;

    public RestoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            title = bundle.getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restore, container, false);

        db = Database.getInstance(getActivity());

        TextView titleTxtv = (TextView) view.findViewById(R.id.restore_titleTxtv_ID);
        titleTxtv.setText(title);

        sum = (PieChart) view.findViewById(R.id.restore_sumPieChart_ID);
        interview = (PieChart) view.findViewById(R.id.restore_interviewPieChart_ID);

        drawSummaryPieChart();
        drawInterviewsPieChart();

        Button restore = (Button) view.findViewById(R.id.restore_Btn_ID);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = new EditText(getContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
                builder.setTitle("Restore old data")
                        .setIcon(R.drawable.ic_baseline_restore_24)
                        .setMessage("Are you sure you want to restore old data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(db.getApplicationsCount() > 0){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
                                    builder.setTitle("Restore old data")
                                            .setIcon(R.drawable.ic_baseline_restore_24)
                                            .setMessage("Please enter description for actual data to archive:")
                                            .setView(name)
                                            .setPositiveButton("Restore and Archive", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    db.moveToArchive(name.getText().toString());
                                                }
                                            })
                                            .setNegativeButton("Cancel", null);

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }

                                db.restore(title);

                                MainBoardActivity activity = (MainBoardActivity)getActivity();
                                BoardFragment fragment = new BoardFragment();
                                activity.setFragmentID(R.layout.fragment_board);
                                activity.setNavigationItemSelected(R.id.nav_board);

                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }

    private void drawSummaryPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        int appSum = db.getArchiveApplicationsCount(title);
        int active = db.getActiveArchiveCount(title);
        int inactive = appSum - active;

        entries.add(new PieEntry((float) active, "Active"));
        entries.add(new PieEntry((float) inactive, "Inactive"));

        PieDataSet set = new PieDataSet(entries, " ");
//        GraphUtil.shuffleColors(colors);
        set.setColors(new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.green)});
        set.setValueTextSize(16f);

        PieData data = new PieData(set);

        sum.setData(data);
        sum.getDescription().setEnabled(false);
        sum.setDrawEntryLabels(false);
        sum.getLegend().setTextColor(getResources().getColor(R.color.lightGray));
        sum.setCenterText("" + appSum);
        sum.setCenterTextSize(18f);
        sum.setCenterTextColor(getResources().getColor(R.color.lightGray));
        sum.setHoleColor(getResources().getColor(R.color.dark_grey));
        sum.animate();
    }

    private void drawInterviewsPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) db.getArchiveApplicationsCount(title) - db.getInterviewArchiveCount(title), "Applications"));
        entries.add(new PieEntry((float) db.getInterviewArchiveCount(title), "Interviews"));

        PieDataSet set = new PieDataSet(entries, " ");
//        GraphUtil.shuffleColors(colors);
        set.setColors(new int[]{getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)});
        set.setValueTextSize(16f);

        PieData data = new PieData(set);

        interview.setData(data);
        interview.getDescription().setEnabled(false);
        interview.setDrawEntryLabels(false);
        interview.getLegend().setTextColor(getResources().getColor(R.color.lightGray));
        interview.setCenterText("" + db.getInterviewArchiveCount(title));
        interview.setCenterTextSize(18f);
        interview.setCenterTextColor(getResources().getColor(R.color.lightGray));
        interview.setHoleColor(getResources().getColor(R.color.dark_grey));
        interview.animate();
    }
}