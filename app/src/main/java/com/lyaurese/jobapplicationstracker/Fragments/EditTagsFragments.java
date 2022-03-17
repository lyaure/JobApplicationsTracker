package com.lyaurese.jobapplicationstracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lyaurese.jobapplicationstracker.Activities.MainBoardActivity;
import com.lyaurese.jobapplicationstracker.Adapters.TagListAdapter;
import com.lyaurese.jobapplicationstracker.Objects.Application;
import com.lyaurese.jobapplicationstracker.Objects.Database;
import com.lyaurese.jobapplicationstracker.Objects.Tag;
import com.lyaurese.jobapplicationstracker.Objects.TagList;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;
import java.util.Iterator;

public class EditTagsFragments extends Fragment {
    private Button save;
    private TextInputEditText search;
    private ListView tagsList;
    private ImageButton close;
    private ArrayList<Tag> tags;
    private TagListAdapter adapter;
    private ArrayList<Tag> tagsCopy;
    private TextView newTag;
    private Application application;
    private String fragmentName;

    public EditTagsFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            application = (Application) bundle.getSerializable("application");
            fragmentName = getArguments().getString("fragmentName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_tags, container, false);

        close = (ImageButton) view.findViewById(R.id.editTagCloseBtn_ID);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        save = (Button) view.findViewById(R.id.editTagSaveBtn_ID);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> returnTags = new ArrayList<>();

                for(Tag tag : tags)
                    if(tag.isChecked())
                        returnTags.add(tag.getName());

                application.setTags(returnTags);
                goBack();
            }
        });

        search = (TextInputEditText) view.findViewById(R.id.searchTagTxtInput_ID);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tagsCopy.addAll(tags);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    clearNewTagView();
                    resetTagsCopy();
                    adapter.notifyDataSetChanged();
                    return;
                }

                resetTagsCopy();

                Iterator<Tag> iter = tagsCopy.iterator();
                while (iter.hasNext()){
                    if (!iter.next().getName().startsWith(s.toString()))
                        iter.remove();
                }

                adapter.notifyDataSetChanged();

                if(tagsCopy.size() != 1) {
                    newTag.setText("Add \"" + s.toString() + "\"");
                    newTag.setVisibility(View.VISIBLE);
                }
                else {
                    if(!tagsCopy.get(0).equals(s.toString())){
                        newTag.setText("Add \"" + s.toString() + "\""     );
                        newTag.setVisibility(View.VISIBLE);
                    }
                    else{
                        clearNewTagView();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newTag = (TextView) view.findViewById(R.id.newTagTxtView_ID);
        newTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTag.getText().toString().isEmpty()){
                    tags.add(0, new Tag(search.getText().toString(), true));
                    search.setText("");
                    newTag.setText("");
                    newTag.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    clearNewTagView();
                    resetTagsCopy();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "New tag cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tagsList = (ListView) view.findViewById(R.id.tagsListView_ID);

        Database db = Database.getInstance(getContext());

        tags = db.getTagsList();
        tagsCopy = new ArrayList<>();
        tagsCopy.addAll(tags);


        adapter = new TagListAdapter(getContext(), tagsCopy);
        tagsList.setAdapter(adapter);

        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tags.get(position).isChecked()) {
                    tags.get(position).setChecked(false);
                    tagsCopy.get(position).setChecked(false);
                } else {
                    tags.get(position).setChecked(true);
                    tagsCopy.get(position).setChecked(true);
                }

                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void clearNewTagView(){
        newTag.setText("");
        newTag.setVisibility(View.GONE);
    }

    private void resetTagsCopy(){
        tagsCopy.clear();
        tagsCopy.addAll(tags);
    }

    private void goBack(){
        Fragment fragment;
        MainBoardActivity activity = (MainBoardActivity)getActivity();

        if(fragmentName.equals("addApplication")) {
            fragment = new AddApplicationFragment();
            activity.setFragmentID(R.layout.fragment_add_application);
        }
        else{
            fragment = new EditApplicationFragment();
            activity.setFragmentID(R.layout.fragment_edit_application);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("application", application);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_ID, fragment, fragmentName); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}