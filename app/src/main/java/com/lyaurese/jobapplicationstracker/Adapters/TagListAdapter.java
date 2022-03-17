package com.lyaurese.jobapplicationstracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyaurese.jobapplicationstracker.Objects.Tag;
import com.lyaurese.jobapplicationstracker.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TagListAdapter extends ArrayAdapter<Tag> {
    private Context context;
    private ArrayList<Tag> tags;

    public TagListAdapter(@NonNull Context context, ArrayList<Tag> list) {
        super(context, 0, list);
        this.context = context;
        this.tags = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(this.context).inflate(R.layout.tag_list_item, parent, false);

        final Tag tag = tags.get(position);

        TextView tagName = (TextView) listItem.findViewById(R.id.tagNameListItem_ID);
        tagName.setText(tag.getName());

        ImageView radioButton = (ImageView) listItem.findViewById(R.id.tagListItemRadioBtn_ID);

        if(tag.isChecked())
            radioButton.setImageResource(R.drawable.ic_baseline_check_circle_24);
        else
            radioButton.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);

        return listItem;
    }
}
