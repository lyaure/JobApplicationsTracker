package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;
import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.Calendar;

public class EditApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private Application application;
    private EditText company, jobTitle, jobNumber, comments, jobLocation;
    private CheckBox interview;
    private LinearLayout appliedDateLayout, interviewDateLayout;
    private TextView appliedDate, interviewDate;
    private Button edit, changeAppliedDateBtn, changeInterviewDateBtn;
    private String oldJobNumber, objectName;
    private boolean changeAppliedDate, changeInterviewDate;
    private int filter, sortOption;
    private ScrollView editScrollView;

    public EditApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            application = (Application) bundle.getSerializable("application");
            filter = getArguments().getInt("filter", -1);
            sortOption = getArguments().getInt("type", 0);
            objectName = getArguments().getString("name");
            oldJobNumber = application.getJobNumber();
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_application, container, false);

        editScrollView = (ScrollView) view.findViewById(R.id.editScrollView_ID);

        company = (EditText) view.findViewById(R.id.editCompanyNameInput_ID);
        company.setText(application.getCompanyName());
        company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                application.setCompanyName(s.toString());
            }
        });

        jobTitle = (EditText) view.findViewById(R.id.editJobTitleInput_ID);
        jobTitle.setText(application.getJobPosition());
        jobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                application.setJobPosition(s.toString());
            }
        });

        jobNumber = (EditText) view.findViewById(R.id.editJobNameInput_ID);
        jobNumber.setText(application.getJobNumber());
        jobNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                application.setJobNumber(s.toString());
            }
        });

        jobLocation = (EditText) view.findViewById(R.id.editJobLocationInput_ID);
        jobLocation.setText(application.getLocation());
        jobLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                application.setLocation(s.toString());
            }
        });

        changeAppliedDate = false;

        appliedDateLayout = (LinearLayout) view.findViewById(R.id.editAppliedDateLayout_ID);
        appliedDate = (TextView) view.findViewById(R.id.editAppliedDateInputTxtv_ID);
        appliedDate.setText(DateUtil.getDate(application.getAppliedDate()));

        changeAppliedDateBtn = (Button) view.findViewById(R.id.appliedChangeDateBtn_ID);
        changeAppliedDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppliedDate = true;
                showDatePickerDialog();
            }
        });

        changeInterviewDate = false;

        interview = (CheckBox) view.findViewById(R.id.editInterviewCheckBox_ID);
        interview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !application.interview()){
                    changeInterviewDate = true;
                    showDatePickerDialog();
                }
                else if(!isChecked && application.interview()){
                    interviewDateLayout.setVisibility(View.GONE);
                    application.setInterviewDate(0);
                    interviewDate.setText("");
                    changeInterviewDate = false;
                }
            }
        });

        interviewDateLayout = (LinearLayout) view.findViewById(R.id.editInterviewDateLayout_ID);
        interviewDate = (TextView) view.findViewById(R.id.editInterviewDateInputTxtv_ID);

        if(application.interview()){
            interview.setChecked(true);
            interviewDateLayout.setVisibility(View.VISIBLE);
            interviewDate.setText(DateUtil.getDate(application.getInterviewDate()));
        }

        changeInterviewDateBtn = (Button) view.findViewById(R.id.interviewChangeDateBtn_ID);
        changeInterviewDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInterviewDate = true;
                showDatePickerDialog();
             }
        });

        comments = (EditText) view.findViewById(R.id.editCommentsInput_ID);
        comments.setText(application.getComment());
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                application.setComment(s.toString());
            }
        });

        comments.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    scrollViewDown(editScrollView);
            }
        });

        edit = (Button) view.findViewById(R.id.finishEditApplicationBtn_ID);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(company.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Company name is a required field.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(jobTitle.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Job position is a required field.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    Database db = Database.getInstance(getActivity());

                    if(application.getJobNumber() != oldJobNumber && db.isJobExists(application.getJobNumber(), application.getCompanyName()) && !application.getJobNumber().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error")
                                .setMessage("Job ID already exists.\nPlease insert a new job ID.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        db.editApplication(application);

                        Fragment fragment = new ApplicationPagerFragment();

                        MainBoardActivity activity = (MainBoardActivity) getActivity();
                        activity.setFragmentID(R.layout.fragment_application);

                        Bundle bundle = new Bundle();
                        //---todo - location/date
                        bundle.putInt("filter", filter);
                        bundle.putInt("type", sortOption);

                        switch (sortOption){
                            case 0:
                                bundle.putString("name", application.getCompanyName());
                                break;
                            case 1:
                                bundle.putString("name", application.getLocation());
                                break;
                        }
                        bundle.putString("application", application.getJobNumber());
                        fragment.setArguments(bundle);

                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container_ID, fragment); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                    }
                }
            }
        });

        return view;
    }

    private void scrollViewDown(ScrollView scrollView){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (getActivity(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!application.interview() && changeInterviewDate){
                    interview.setChecked(false);
                    interviewDateLayout.setVisibility(View.GONE);
                    changeInterviewDate = false;
                    application.setInterviewDate(0);
                }
            }
        });

        datePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(changeAppliedDate) {
            appliedDate.setText(DateUtil.getDate(calendar.getTimeInMillis()));
            application.setAppliedDate(calendar.getTimeInMillis());
            changeAppliedDate = false;
        }

        if(changeInterviewDate) {
            interviewDate.setText(DateUtil.getDate(calendar.getTimeInMillis()));
            interviewDateLayout.setVisibility(View.VISIBLE);
            application.setInterviewDate(calendar.getTimeInMillis());
            changeInterviewDate = false;
        }

        scrollViewDown(editScrollView);
    }
}
