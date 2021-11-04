package com.lyaurese.jobsorganizer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyaurese.jobsorganizer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddApplicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddApplicationFragment extends Fragment {

    public AddApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_application, container, false);
    }
}