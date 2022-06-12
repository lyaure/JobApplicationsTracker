package com.lyaurese.jobapplicationstracker.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        Button archive = (Button) view.findViewById(R.id.archiveData_btn_ID);

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = new EditText(getContext());

                Database db = Database.getInstance(getActivity());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
                builder.setTitle("Archive data")
                        .setIcon(R.drawable.ic_baseline_archive_24)
                        .setMessage("Please enter description for actual data to archive:")
                        .setView(name)
                        .setPositiveButton("Archive", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.moveToArchive(name.getText().toString());

                                MainBoardActivity activity = (MainBoardActivity)getActivity();
                                BoardFragment fragment = new BoardFragment();
                                activity.setFragmentID(R.layout.fragment_board);
                                activity.setNavigationItemSelected(R.id.nav_board);

                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container_ID, fragment ); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }
}