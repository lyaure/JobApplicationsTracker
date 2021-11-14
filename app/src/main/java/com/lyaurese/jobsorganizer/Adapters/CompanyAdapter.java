package com.lyaurese.jobsorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lyaurese.jobsorganizer.Activities.MainBoardActivity;
import com.lyaurese.jobsorganizer.Fragments.ApplicationPagerFragment;
import com.lyaurese.jobsorganizer.Objects.Company;
import com.lyaurese.jobsorganizer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class CompanyAdapter extends ArrayAdapter<Company> {
    private Context context;
    private List<Company> companyList;
    private MainBoardActivity activity;

    public CompanyAdapter(@NonNull Context context, ArrayList<Company> list) {
        super(context, 0, list);
        this.context = context;
        this.companyList = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        final Company company = companyList.get(position);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationPagerFragment fragment = new ApplicationPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("company", company.getName());
                bundle.putString("application", "none");
                fragment.setArguments(bundle);

                FragmentActivity fragmentActivity = (FragmentActivity) v.getContext();
                activity.setFragmentID(R.layout.fragment_application_pager);

                FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_ID, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView name = (TextView)listItem.findViewById(R.id.companyName_ID);
        name.setText(company.getName());

        TextView num = (TextView)listItem.findViewById(R.id.numOfApplication_ID);
        num.setText(Integer.toString(company.getNumOfApplications()));

        return listItem;
    }

    public void setActivity(MainBoardActivity activity) {
        this.activity = activity;
    }
}
