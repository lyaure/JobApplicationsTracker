package com.lyaurese.jobsorganizer.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyaurese.jobsorganizer.Activities.MainBoardActivity;
import com.lyaurese.jobsorganizer.Objects.Application;
import com.lyaurese.jobsorganizer.R;
import com.lyaurese.jobsorganizer.Utils.DateUtil;

import java.util.Calendar;


public class ApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private Application application;
    private ImageButton editButton;
    private TextView company, title, number, appliedDate, interviewedDate, comments;
    private Calendar calendar;


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

//        editButton = (ImageButton) view.findViewById(R.id.editApplicationBtn_ID);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new EditApplicationFragment();
//
//                MainBoardActivity activity = (MainBoardActivity)getActivity();
//                activity.setFragmentID(R.layout.fragment_edit_application);
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("application", application);
//                fragment.setArguments(bundle);
//
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();
//            }
//        });

        company = (TextView) view.findViewById(R.id.companyNameTxtv_ID);
        company.setText(application.getCompanyName());

        title = (TextView) view.findViewById(R.id.jobTitleTxtv_ID);
        title.setText(application.getJobTitle());

        number = (TextView) view.findViewById(R.id.jobNumberTxtv_ID);
        number.setText(application.getJobNumber());

        appliedDate = (TextView) view.findViewById(R.id.appliedDateTxtv_ID);
        if(application.getAppliedDate() != null)
            appliedDate.setText("Applied on: " +DateUtil.getDate(application.getAppliedDate()));
        else
            appliedDate.setText("Didn't applied yet");

        interviewedDate = (TextView) view.findViewById(R.id.interviewDateTxtv_ID);
        if(application.getInterviewDate() != null)
            interviewedDate.setText("Got an interview on: " + DateUtil.getDate(application.getInterviewDate()));
        else
            interviewedDate.setText("Didn't get a response");

        comments = (TextView) view.findViewById(R.id.commentsInput_ID);
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