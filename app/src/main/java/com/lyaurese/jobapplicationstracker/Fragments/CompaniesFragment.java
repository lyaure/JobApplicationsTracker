package com.lyaurese.jobapplicationstracker.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.lyaurese.jobapplicationstracker.Objects.ListObject;
import com.lyaurese.jobapplicationstracker.Adapters.ListObjectAdapter;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;


public class CompaniesFragment extends Fragment {
    private ArrayList<ListObject> list;
    private ListView companyList;
    private LinearLayout noData;
    private ListObjectAdapter adapter;
    private TextView filterTxtv, displayTxtv;
    private SharedPreferences sp;
    private int filter, filterOption, sortOption;
    private String sortOptions[];
    private Database db;

    private final int ALL = -1, ACTIVE = 1, INACTIVE = 0;
    private final int COMPANY = 0, LOCATION = 1, MONTHS = 2, LAST_SEVEN_DAYS = 3;


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
        sortOption = sp.getInt("sortOption", 0);
//        int filterOption = 0;

        ImageButton addApplicationBtn = (ImageButton) view.findViewById(R.id.addApplicationBtn_ID);
        addApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addApplication = new AddApplicationFragment();

                MainBoardActivity activity = (MainBoardActivity)getActivity();
                activity.setFragmentID(R.layout.fragment_add_application);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, addApplication, "addApplication"); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        displayTxtv = (TextView) view.findViewById(R.id.displayTxtv_ID);
        filterTxtv = (TextView) view.findViewById(R.id.filterTxtv_ID);

        sortOptions = new String[]{"Company", "Location"};
        String filterOptions[] = new String[]{"All", "Active", "Inactive"};

        displayTxtv.setText("by " + sortOptions[sortOption]);
        filterTxtv.setText(filterOptions[filterOption]);

        TextView displayBtn = (TextView) view.findViewById(R.id.displayBtn_ID);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOption = sp.getInt("sortOption", COMPANY);
                showOptions("sort", R.drawable.ic_baseline_tune_24, sortOptions, sortOption, displayTxtv);
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

        db = Database.getInstance(getActivity());

        list = getList();

        checkEmptyCompaniesList();

        adapter = new ListObjectAdapter(getContext(), list);
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
                        SharedPreferences.Editor editor = sp.edit();

                        if(option.equals("filter")){
                            textView.setText(items[which]);

                            editor.putInt("filterOption", which);

                            switch (which){
                                case 0:
                                    editor.putInt("filter", ALL);
                                    filter = ALL;
                                    break;
                                case 1:
                                    editor.putInt("filter", ACTIVE);
                                    filter = ACTIVE;
                                    break;
                                case 2:
                                    editor.putInt("filter", INACTIVE);
                                    filter = INACTIVE;
                                    break;
                            }
                        }
                        else {
                            sortOption = which;
                            editor.putInt("sortOption", which);
                            textView.setText("by " + items[which]);
                        }

                        editor.commit();

                        list.clear();
                        list.addAll(getList());
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

        if(list.size() != 0){
            companyList.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

        }
        else{
            companyList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<ListObject> getList(){
        int filter = sp.getInt("filter", ALL);

        switch (sortOption){
            case COMPANY:
                return db.getCompaniesList(filter);
            case LOCATION:
                return db.getLocationsList(filter);
            case MONTHS:
                return null;
            default:
                return db.getCompaniesList(filter);
        }
    }
}

