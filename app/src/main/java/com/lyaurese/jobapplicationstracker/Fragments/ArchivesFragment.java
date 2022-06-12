package com.lyaurese.jobapplicationstracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Adapters.ListObjectAdapter;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.Objects.ListObject;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;

public class ArchivesFragment extends Fragment {

    public ArchivesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archives, container, false);

        ListView archiveTableNames = (ListView) view.findViewById(R.id.archives_listView_ID);

        Database db = Database.getInstance(getActivity());
        ArrayList<ListObject> names = db.getArchiveTableNames();

        if(names != null){
            ListObjectAdapter adapter = new ListObjectAdapter(getContext(), names);
            adapter.setActivity((MainBoardActivity) getActivity());
            archiveTableNames.setAdapter(adapter);
        }

        return view;
    }
}