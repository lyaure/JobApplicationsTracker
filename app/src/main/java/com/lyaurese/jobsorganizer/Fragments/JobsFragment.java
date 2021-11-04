package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lyaurese.jobsorganizer.Objects.Company;
import com.lyaurese.jobsorganizer.Objects.CompanyAdapter;
import com.lyaurese.jobsorganizer.R;

import java.util.ArrayList;


public class JobsFragment extends Fragment {
    private ArrayList<Company> companies = new ArrayList<>();
    private ListView companyList;
    private CompanyAdapter adapter;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_jobs, container, false);

        companies.add(new Company("A", 1));
        companies.add(new Company("B", 2));
        companies.add(new Company("C", 3));

        companyList = (ListView) view.findViewById(R.id.companyList_ID);

        adapter = new CompanyAdapter(getContext(), companies);
        companyList.setAdapter(adapter);

        return view;
    }
}