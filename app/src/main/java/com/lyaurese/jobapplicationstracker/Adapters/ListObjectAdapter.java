package com.lyaurese.jobapplicationstracker.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Fragments.ApplicationPagerFragment;
import com.lyaurese.jobapplicationstracker.Objects.ListObject;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class ListObjectAdapter extends ArrayAdapter<ListObject> {
    private Context context;
    private List<ListObject> list;
    private MainBoardActivity activity;

    public ListObjectAdapter(@NonNull Context context, ArrayList<ListObject> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        final ListObject object = list.get(position);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationPagerFragment fragment = new ApplicationPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("company", object.getName());
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
        name.setText(object.getName());

        TextView num = (TextView)listItem.findViewById(R.id.numOfApplication_ID);
        num.setText(Integer.toString(object.getNumOfApplications()));

        return listItem;
    }

    public void setActivity(MainBoardActivity activity) {
        this.activity = activity;
    }
}
