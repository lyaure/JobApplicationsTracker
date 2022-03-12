package com.lyaurese.jobapplicationstracker.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;
import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.Calendar;


public class AddApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private EditText company, jobTitle, jobNumber, comments, jobLocation;
    private Button changeDate;
    private LinearLayout dateLayout;
    private TextView date;
    private Button add;
    private Database db;
    private Calendar calendar;
    private ScrollView addScrollView;

    public AddApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_application, container, false);

        addScrollView = (ScrollView) view.findViewById(R.id.addScrollView_ID);

        company = (EditText) view.findViewById(R.id.companyNameInput_ID);
        jobTitle = (EditText) view.findViewById(R.id.jobTitleInput_ID);
        jobNumber = (EditText) view.findViewById(R.id.jobNameInput_ID);
        jobLocation = (EditText) view.findViewById(R.id.jobNLocationInput_ID);
        changeDate = (Button) view.findViewById(R.id.changeApplicationDateBtn_ID);
        dateLayout = (LinearLayout) view.findViewById(R.id.appliedDateLayout_ID);
        date = (TextView) view.findViewById(R.id.dateInputTxtv_ID);
        comments = (EditText) view.findViewById(R.id.commentsInput_ID);
        add = (Button) view.findViewById(R.id.finishAddApplicationBtn_ID);

        db = Database.getInstance(getActivity());;

        calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());

        date.setText(String.format(DateUtil.getDate(calendar.getTimeInMillis())));

        changeDate.setOnClickListener(new View.OnClickListener() {
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
                String commentsInput = comments.getText().toString();
                String locationInput = jobLocation.getText().toString();

                if(companyInput.equals("")){
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
                else if(jobTitleInput.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Job Position is a required field.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(db.isJobExists(jobNumberInput, companyInput) && !jobNumberInput.equals("")){
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
                        MainBoardActivity activity = (MainBoardActivity)getActivity();
                        activity.setFragmentID(R.layout.fragment_companies);

                        SharedPreferences sp = activity.getSharedPreferences("id", Context.MODE_PRIVATE);
                        int id = sp.getInt("lastId", -1) + 1;

                        //---TODO---- remove applied property
                        Application application = new Application(id, companyInput, jobTitleInput, jobNumberInput, locationInput, calendar.getTimeInMillis(), commentsInput);

                        db.insertNewApplication(application);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastId", id);
                        editor.commit();

                        Fragment CompaniesFragment = new CompaniesFragment();

                        activity.setFragmentID(R.layout.fragment_companies);

                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container_ID, CompaniesFragment); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
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

        datePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date.setText(String.format(DateUtil.getDate(calendar.getTimeInMillis())));
        scrollViewDown(addScrollView);
    }
}