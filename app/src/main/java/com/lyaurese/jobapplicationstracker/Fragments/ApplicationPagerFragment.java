package com.lyaurese.jobapplicationstracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Adapters.ViewPagerAdapter;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class ApplicationPagerFragment extends Fragment {
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private String companyName, jobNumber;
    private ImageButton backButton;
    private ArrayList<Application> applicationsList = new ArrayList<>();
    private Database db;

    public ApplicationPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            companyName = getArguments().getString("company");
            jobNumber = getArguments().getString("application");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_application_pager, container, false);

        db = new Database(getContext());

        ImageButton editButton = (ImageButton) view.findViewById(R.id.editApplicationBtn_ID);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteFragmentBtn_ID);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = viewPager.getCurrentItem();
                db.removeApplication(applicationsList.get(index));

                if(applicationsList.size() == 1){
                    CompaniesFragment fragment = new CompaniesFragment();

                    FragmentActivity fragmentActivity = (FragmentActivity) v.getContext();
                    MainBoardActivity activity = new MainBoardActivity();
                    activity.setFragmentID(R.layout.fragment_companies);

                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_ID, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    ApplicationPagerFragment fragment = new ApplicationPagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("company", applicationsList.get(index).getCompanyName());
                    bundle.putString("application", applicationsList.get(index == 0? index+1 : index-1).getJobNumber());
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_ID, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        // multiple fragments in this fragment
        viewPager = (ViewPager2) view.findViewById(R.id.applicationsViewpager_ID);
        adapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);

        // indicates number  of fragments
        DotsIndicator dotsIndicator = (DotsIndicator) view.findViewById(R.id.applicationsDotsIndicator_ID);
        dotsIndicator.setViewPager2(viewPager);

        Database db = new Database(getContext());

        applicationsList = db.getApplicationList(companyName);

        int index = -1;

        for(Application application : applicationsList){
            if(jobNumber.equals(application.getJobNumber()))
                index = applicationsList.indexOf(application);
            ApplicationFragment fragment = new ApplicationFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("application", application);
            fragment.setArguments(bundle);

            adapter.addFrag(fragment);
            adapter.notifyDataSetChanged();
        }

        if(index != -1)
            viewPager.setCurrentItem(index);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = viewPager.getCurrentItem();

                Fragment fragment = new EditApplicationFragment();

                MainBoardActivity activity = (MainBoardActivity)getActivity();
                activity.setFragmentID(R.layout.fragment_edit_application);

                Bundle bundle = new Bundle();
                bundle.putSerializable("application", applicationsList.get(index));
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });


        backButton = (ImageButton)view.findViewById(R.id.pagerBackBtn_ID);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CompaniesFragment();

                MainBoardActivity activity = (MainBoardActivity)getActivity();
                activity.setFragmentID(R.layout.fragment_companies);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });


        return view;
    }
}