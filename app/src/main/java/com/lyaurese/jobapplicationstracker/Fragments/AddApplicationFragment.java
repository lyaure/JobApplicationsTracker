package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;

import java.util.Calendar;


public class AddApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private EditText company, jobTitle, jobNumber, comments;
    private CheckBox applied;
    private LinearLayout dateLayout;
    private TextView date;
    private Button add;
    private Database db;
    private Calendar calendar;

    public AddApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_application, container, false);

        company = (EditText) view.findViewById(R.id.companyNameInput_ID);
        jobTitle = (EditText) view.findViewById(R.id.jobTitleInput_ID);
        jobNumber = (EditText) view.findViewById(R.id.jobNameInput_ID);
        applied = (CheckBox) view.findViewById(R.id.appliedCheckBox_ID);
        dateLayout = (LinearLayout) view.findViewById(R.id.appliedDateLayout_ID);
        date = (TextView) view.findViewById(R.id.dateInputTxtv_ID);
        comments = (EditText) view.findViewById(R.id.commentsInput_ID);
        add = (Button) view.findViewById(R.id.finishAddApplicationBtn_ID);

        db = new Database(getContext());

        calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());

        applied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (applied.isChecked()) {
                    showDatePickerDialog();
                }
                else
                    dateLayout.setVisibility(View.INVISIBLE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyInput = company.getText().toString();
                String jobTitleInput = jobTitle.getText().toString();
                String jobNumberInput = jobNumber.getText().toString();
                boolean appliedInput = applied.isChecked();
                String commentsInput = comments.getText().toString();

                if(companyInput.equals("") || jobNumberInput.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Company name and job number are required fields.")
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
                    if(db.isJobExists(jobNumberInput)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error")
                                .setMessage("Application with the same job number already exists.\nPlease insert a new job number.")
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
                        Application application = new Application(companyInput, jobTitleInput, jobNumberInput, appliedInput, calendar, commentsInput);

                        db.insertNewApplication(application);

                        Fragment CompaniesFragment = new CompaniesFragment();

                        MainBoardActivity activity = (MainBoardActivity) getActivity();
                        activity.setFragmentID(R.layout.fragment_companies);

                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container_ID, CompaniesFragment); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                    }
                }


            }
        });

        return view;
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (getActivity(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applied.setChecked(false);
            }
        });

        datePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String s = "%d/%d/%d";

        date.setText(String.format(s, dayOfMonth, month + 1, year));
        dateLayout.setVisibility(View.VISIBLE);

        calendar.set(year, month, dayOfMonth);
    }
}