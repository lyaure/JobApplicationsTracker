package com.lyaurese.jobsorganizer.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lyaurese.jobsorganizer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CompanyAdapter extends ArrayAdapter<Company> {
    private Context context;
    private List<Company> companyList;

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

        TextView name = (TextView)listItem.findViewById(R.id.companyName_ID);
        name.setText(company.getName());

        TextView num = (TextView)listItem.findViewById(R.id.numOfApplication_ID);
        num.setText(Integer.toString(company.getNumOfApplications()));

        return listItem;
    }
}
