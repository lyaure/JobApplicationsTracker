package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;
import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.Calendar;


public class ApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private Application application;
    private ImageButton editButton;
    private TextView company, title, number, appliedDate, interviewedDate, appliedTxt, interviewTxt, comments, location;
    private Calendar calendar;
    private CheckBox active;


    public ApplicationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            application = (Application) bundle.getSerializable("application");
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        company = (TextView) view.findViewById(R.id.companyNameTxtv_ID);
        company.setText(application.getCompanyName());

        title = (TextView) view.findViewById(R.id.jobTitleTxtv_ID);
        title.setText(application.getJobPosition());

        number = (TextView) view.findViewById(R.id.jobNumberTxtv_ID);
        number.setText(application.getJobNumber());

        location = (TextView) view.findViewById(R.id.jobLocationTxtv_ID);
        location.setText(application.getLocation());

        appliedTxt = (TextView) view.findViewById(R.id.appliedTxt_ID);

        appliedDate = (TextView) view.findViewById(R.id.appliedDateTxtv_ID);
        if(application.getAppliedDate() != null)
            appliedDate.setText(DateUtil.getDate(application.getAppliedDate()));
        else
            appliedDate.setText("Didn't applied yet");

        interviewTxt = (TextView) view.findViewById(R.id.interviewTxt_ID);

        interviewedDate = (TextView) view.findViewById(R.id.interviewDateTxtv_ID);
        if(application.getInterviewDate() != null)
            interviewedDate.setText(DateUtil.getDate(application.getInterviewDate()));
        else
            interviewedDate.setText("-");

        active = (CheckBox) view.findViewById(R.id.activeChkbx_ID);
        active.setChecked(application.isActive());

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Database db = new Database(getContext());
                application.setActive(isChecked);
                db.setActive(application.getJobNumber(), isChecked);
            }
        });

        comments = (TextView) view.findViewById(R.id.commentsInput_ID);
        comments.setMovementMethod(new ScrollingMovementMethod());
        String s = application.getComment();
        comments.setText(s);

        return view;
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (getActivity(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String s = "Interview on: %d/%d/%d";

        interviewedDate.setText(String.format(s, dayOfMonth, month + 1, year));

        calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
    }
}