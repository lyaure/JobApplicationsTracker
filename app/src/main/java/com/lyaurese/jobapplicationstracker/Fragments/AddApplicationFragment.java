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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;
import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;


public class AddApplicationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private EditText company, jobPosition, jobNumber, comments, jobLocation;
    private Button changeDate;
    private LinearLayout dateLayout;
    private TextView date, tags;
    private Button add;
    private Database db;
    private Calendar calendar;
    private ScrollView addScrollView;
    private ImageButton editTags;
    private Application tempApplication;

    public AddApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            tempApplication = (Application) bundle.getSerializable("application");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_application, container, false);

        addScrollView = (ScrollView) view.findViewById(R.id.addScrollView_ID);

        company = (EditText) view.findViewById(R.id.companyNameInput_ID);
        jobPosition = (EditText) view.findViewById(R.id.jobTitleInput_ID);
        jobNumber = (EditText) view.findViewById(R.id.jobNameInput_ID);
        jobLocation = (EditText) view.findViewById(R.id.jobNLocationInput_ID);
        tags = (TextView) view.findViewById(R.id.addApplicationTagsTxtView_ID);
        editTags = (ImageButton) view.findViewById(R.id.addApplicationEditTagsBtn_ID);
        changeDate = (Button) view.findViewById(R.id.changeApplicationDateBtn_ID);
        dateLayout = (LinearLayout) view.findViewById(R.id.appliedDateLayout_ID);
        date = (TextView) view.findViewById(R.id.dateInputTxtv_ID);
        comments = (EditText) view.findViewById(R.id.commentsInput_ID);
//        comments.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus)
//                    scrollViewDown(addScrollView);
//            }
//        });
        add = (Button) view.findViewById(R.id.finishAddApplicationBtn_ID);

        editTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempApplication = getTempApplication(-1);

                EditTagsFragments fragment = new EditTagsFragments();

                MainBoardActivity activity = (MainBoardActivity) getActivity();
                activity.setFragmentID(R.layout.fragment_edit_tags);

                Bundle bundle = new Bundle();
                bundle.putSerializable("application", tempApplication);
                bundle.putString("fragmentName", "addApplication");
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, fragment, "editTags"); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        db = Database.getInstance(getActivity());

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
                String jobTitleInput = jobPosition.getText().toString();
                String jobNumberInput = jobNumber.getText().toString();
                String commentsInput = comments.getText().toString();
                String locationInput = jobLocation.getText().toString();

                if (companyInput.equals("")) {
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
                } else if (jobTitleInput.equals("")) {
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
                } else if (db.isJobExists(jobNumberInput, companyInput) && !jobNumberInput.equals("")) {
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
                } else {
                    Application application = new Application(-1, companyInput, jobTitleInput, jobNumberInput, locationInput, calendar.getTimeInMillis(), commentsInput);
                    application.setTags(tempApplication != null ? tempApplication.getTags() : new ArrayList<String>());

                    Long id = db.insertNewApplication(application);

                    Fragment fragment = new ApplicationPagerFragment();

                    MainBoardActivity activity = (MainBoardActivity) getActivity();
                    activity.setFragmentID(R.layout.fragment_application);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", application.getCompanyName());
                    bundle.putLong("application", id);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_ID, fragment); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }
            }
        });

        if (tempApplication != null)
            updateViews();

        return view;
    }

    private void scrollViewDown(ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void showDatePickerDialog() {
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

    private Application getTempApplication(int id) {
        String companyInput = company.getText().toString();
        String jobTitleInput = jobPosition.getText().toString();
        String jobNumberInput = jobNumber.getText().toString();
        String commentsInput = comments.getText().toString();
        String locationInput = jobLocation.getText().toString();

        return new Application(id, companyInput, jobTitleInput, jobNumberInput, locationInput, calendar.getTimeInMillis(), commentsInput);
    }

    private void updateViews() {
        company.setText(tempApplication.getCompanyName());
        jobPosition.setText(tempApplication.getJobPosition());
        jobNumber.setText(tempApplication.getJobNumber());
        jobLocation.setText(tempApplication.getLocation());
        tags.setText(tempApplication.tagsToString());
        date.setText(DateUtil.getDate(tempApplication.getAppliedDate()));
        comments.setText(tempApplication.getComment());
    }
}