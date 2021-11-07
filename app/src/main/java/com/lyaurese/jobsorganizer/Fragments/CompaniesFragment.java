package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lyaurese.jobsorganizer.Activities.MainBoardActivity;
import com.lyaurese.jobsorganizer.Objects.Company;
import com.lyaurese.jobsorganizer.Adapters.CompanyAdapter;
import com.lyaurese.jobsorganizer.Objects.Database;
import com.lyaurese.jobsorganizer.R;

import java.util.ArrayList;


public class CompaniesFragment extends Fragment {
    private ArrayList<Company> companies;
    private ListView companyList;
    private LinearLayout noData;
    private CompanyAdapter adapter;
    private ImageButton addApplicationBtn;

    public CompaniesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_companies, container, false);

        MainBoardActivity activity = (MainBoardActivity)getActivity();
        activity.setFragmentID(R.layout.fragment_companies);

        addApplicationBtn = (ImageButton) view.findViewById(R.id.addApplicationBtn_ID);
        addApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addApplication = new AddApplicationFragment();

                MainBoardActivity activity = (MainBoardActivity)getActivity();
                activity.setFragmentID(R.layout.fragment_add_application);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, addApplication ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        noData = (LinearLayout) view.findViewById(R.id.noDataLayout_ID);
        companyList = (ListView) view.findViewById(R.id.companyList_ID);

        Database db = new Database(getContext());
        companies = db.getCompanyList();

        if(companies != null){
            companyList.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

            adapter = new CompanyAdapter(getContext(), companies);
            companyList.setAdapter(adapter);
        }
        else{
            companyList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }

        return view;
    }
}