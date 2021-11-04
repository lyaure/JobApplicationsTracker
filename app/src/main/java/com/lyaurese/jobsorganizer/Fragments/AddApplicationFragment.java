package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.lyaurese.jobsorganizer.Objects.Database;
import com.lyaurese.jobsorganizer.R;

import java.util.Objects;

public class AddApplicationFragment extends Fragment {
    private EditText company;
    private EditText jobTitle;
    private EditText jobNumber;
    private CheckBox applied;
    private Button add;
    private Database db;

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
        add = (Button) view.findViewById(R.id.finishAddApplicationBtn_ID);

        db = new Database(getContext());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyInput = company.getText().toString();
                String jobTitleInput = jobTitle.getText().toString();
                String jobNumberInput = jobNumber.getText().toString();
                int appliedInput = applied.isChecked()? 1 : 0;

                db.insertNewApplication(companyInput, jobTitleInput, jobNumberInput, appliedInput, 1, 1, 1, "no comment");

                Fragment CompaniesFragment = new CompaniesFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, CompaniesFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        return view;
    }
}