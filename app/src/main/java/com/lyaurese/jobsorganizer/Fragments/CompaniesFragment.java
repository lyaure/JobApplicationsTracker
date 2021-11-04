package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.lyaurese.jobsorganizer.Objects.Company;
import com.lyaurese.jobsorganizer.Objects.CompanyAdapter;
import com.lyaurese.jobsorganizer.R;

import java.util.ArrayList;


public class CompaniesFragment extends Fragment {
    private ArrayList<Company> companies = new ArrayList<>();
    private ListView companyList;
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

        addApplicationBtn = (ImageButton) view.findViewById(R.id.addApplicationBtn_ID);
        addApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addApplication = new AddApplicationFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, addApplication ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });


        companies.add(new Company("A", 1));
        companies.add(new Company("B", 2));
        companies.add(new Company("C", 3));

        companyList = (ListView) view.findViewById(R.id.companyList_ID);

        adapter = new CompanyAdapter(getContext(), companies);
        companyList.setAdapter(adapter);



        return view;
    }
}