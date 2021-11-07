package com.lyaurese.jobsorganizer.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import com.google.android.material.textfield.TextInputLayout;
import com.lyaurese.jobsorganizer.Activities.MainBoardActivity;
import com.lyaurese.jobsorganizer.Objects.Application;
import com.lyaurese.jobsorganizer.Objects.Company;
import com.lyaurese.jobsorganizer.Objects.Database;
import com.lyaurese.jobsorganizer.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

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
        dateLayout = (LinearLayout) view.findViewById(R.id.dateLayout_ID);
        date = (TextView) view.findViewById(R.id.dateInputTxtv_ID);
        comments = (EditText) view.findViewById(R.id.commentsInput_ID);
        add = (Button) view.findViewById(R.id.finishAddApplicationBtn_ID);

        db = new Database(getContext());

        calendar = new GregorianCalendar();
        calendar.setTime(Calendar.getInstance().getTime());

        applied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (applied.isChecked()) {
                    dateLayout.setVisibility(View.VISIBLE);

                    String s = "%d/%d/%d";

                    date.setText(String.format(s, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
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

                Application application = new Application(companyInput, jobTitleInput, jobNumberInput, appliedInput, calendar, commentsInput);

                db.insertNewApplication(application);

                Fragment CompaniesFragment = new CompaniesFragment();

                MainBoardActivity activity = (MainBoardActivity)getActivity();
                activity.setFragmentID(R.layout.fragment_companies);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, CompaniesFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

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
        String s = "%d/%d/%d";

        date.setText(String.format(s, dayOfMonth, month + 1, year));

        calendar.set(year, month, dayOfMonth);
    }
}