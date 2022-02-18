package com.lyaurese.jobapplicationstracker.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Activities.SplashActivity;
import com.lyaurese.jobapplicationstracker.Objects.Company;
import com.lyaurese.jobapplicationstracker.Adapters.CompanyAdapter;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;


public class CompaniesFragment extends Fragment {
    private ArrayList<Company> companies;
    private ListView companyList;
    private LinearLayout noData;
    private CompanyAdapter adapter;
    private TextView filterTxtv, displayTxtv;
    private SharedPreferences sp;
    private int filter, filterOption, displayOption;
    private String display;
    private Database db;

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

        sp = activity.getSharedPreferences("applications filter", Context.MODE_PRIVATE);
        filterOption = sp.getInt("filterOption", 0);
        displayOption = sp.getInt("displayOption", 0);
//        int filterOption = 0;

        ImageButton addApplicationBtn = (ImageButton) view.findViewById(R.id.addApplicationBtn_ID);
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

        displayTxtv = (TextView) view.findViewById(R.id.displayTxtv_ID);
        filterTxtv = (TextView) view.findViewById(R.id.filterTxtv_ID);

        String displayOptions[] = new String[]{"Company", "Location", "Date"};
        String filterOptions[] = new String[]{"All", "Active", "Inactive"};

        displayTxtv.setText("by " + displayOptions[displayOption]);
        filterTxtv.setText(filterOptions[filterOption]);

        TextView displayBtn = (TextView) view.findViewById(R.id.displayBtn_ID);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOption = sp.getInt("displayOption", 0);
                showOptions("sort", R.drawable.ic_baseline_tune_24, displayOptions, displayOption, displayTxtv);
            }
        });

        TextView filterBtn = (TextView) view.findViewById(R.id.filterBtn_ID);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOption = sp.getInt("filterOption", 0);
                showOptions("filter", R.drawable.ic_baseline_filter_alt_24, filterOptions, filterOption, filterTxtv);
            }
        });

        noData = (LinearLayout) view.findViewById(R.id.noDataLayout_ID);
        companyList = (ListView) view.findViewById(R.id.companyList_ID);

        db = new Database(getContext());

        filter = sp.getInt("filter", -1);
        companies = db.getCompaniesListWithFilter(filter);

        checkEmptyCompaniesList();

        adapter = new CompanyAdapter(getContext(), companies);
        adapter.setActivity((MainBoardActivity) getActivity());
        companyList.setAdapter(adapter);

        return view;
    }

    private void showOptions(String option, int icon, String[] items, int checkedItem, TextView textView){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
        builder.setTitle(option)
                .setIcon(icon)
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(option.equals("filter")){
                            textView.setText(items[which]);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("filterOption", which);

                            switch (which){
                                case 0:
                                    editor.putInt("filter", -1);
                                    filter = -1;
                                    break;
                                case 1:
                                    editor.putInt("filter", 1);
                                    filter = 1;
                                    break;
                                case 2:
                                    editor.putInt("filter", 0);
                                    filter = 0;
                                    break;
                            }
                            editor.commit();
                        }
                        else
                            textView.setText("by " + items[which]);

                        companies.clear();
                        companies.addAll(db.getCompaniesListWithFilter(filter));
                        adapter.notifyDataSetChanged();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 200);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkEmptyCompaniesList(){

        if(companies.size() != 0){
            companyList.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

        }
        else{
            companyList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }
}

