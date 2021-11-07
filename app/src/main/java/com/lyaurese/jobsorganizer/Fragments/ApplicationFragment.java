package com.lyaurese.jobsorganizer.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyaurese.jobsorganizer.Objects.Application;
import com.lyaurese.jobsorganizer.R;

import java.util.Calendar;

public class ApplicationFragment extends Fragment {
    private Application application;
    private ImageButton editButton;
    private TextView company, title, number, appliedDate, interviewedDate;
    private CheckBox interviewed;
    private EditText comments;


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

        editButton = (ImageButton) view.findViewById(R.id.editApplicationBtn_ID);

        company = (TextView) view.findViewById(R.id.companyNameTxtv_ID);
        company.setText(application.getCompanyName());

        title = (TextView) view.findViewById(R.id.jobTitleTxtv_ID);
        title.setText(application.getJobTitle());

        number = (TextView) view.findViewById(R.id.jobNumberTxtv_ID);
        number.setText(application.getJobNumber());

        appliedDate = (TextView) view.findViewById(R.id.appliedDateTxtv_ID);
        String s = "Applied on: %d/%d/%d";
        appliedDate.setText(String.format(s, application.getAppliedDate().get(Calendar.DAY_OF_MONTH), application.getAppliedDate().get(Calendar.MONTH), application.getAppliedDate().get(Calendar.YEAR)));

        interviewedDate = (TextView) view.findViewById(R.id.interviewedDateTxtv_ID);

        interviewed = (CheckBox) view.findViewById(R.id.gotInterviewedCheckbox_ID);
        interviewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(interviewed.isChecked()){
                    interviewed.setVisibility(View.GONE);
                    interviewedDate.setVisibility(View.VISIBLE);
                    //----TODO-----
                    // add date picker and change interviewed date value
                    String s = "Interviewed on: 01/01/2021";
                    interviewedDate.setText(s);
                }
            }
        });

        comments = (EditText) view.findViewById(R.id.commentsInput_ID);
        s = application.getComment();
        comments.setText(s);

        return view;
    }
}