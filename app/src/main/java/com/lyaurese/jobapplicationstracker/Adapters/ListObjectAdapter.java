package com.lyaurese.jobapplicationstracker.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Fragments.ApplicationPagerFragment;
import com.lyaurese.jobapplicationstracker.Fragments.RestoreFragment;
import com.lyaurese.jobapplicationstracker.Objects.ListObject;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
                Bundle bundle = new Bundle();
                Fragment fragment;
                FragmentActivity fragmentActivity = (FragmentActivity) v.getContext();

                if(object.getType() != -1){
                    SharedPreferences sp = activity.getSharedPreferences("applications filter", Context.MODE_PRIVATE);

                    fragment = new ApplicationPagerFragment();
                    bundle.putInt("filter", sp.getInt("filter", -1));
                    bundle.putInt("type", object.getType());
                    bundle.putString("name", object.getName());

                    activity.setFragmentID(R.layout.fragment_application_pager);
                }
                else{
                    fragment = new RestoreFragment();
                    bundle.putString("title", object.getName());

                    activity.setFragmentID(R.layout.fragment_restore);
                }
                fragment.setArguments(bundle);

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
