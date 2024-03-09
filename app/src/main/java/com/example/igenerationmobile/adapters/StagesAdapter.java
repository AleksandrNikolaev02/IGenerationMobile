package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.model.ExpandableListModel.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StagesAdapter extends BaseExpandableListAdapter {

    private Context context;

    public List<String> groupData;

    public Map<String, List<Stage>> childData;


    public StagesAdapter(Context context, List<String> groupData, Map<String, List<Stage>> childData) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return childData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childData.get(groupData.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String stageName = getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_groupview, null);
        }

        TextView textGroup = convertView.findViewById(R.id.question);
        textGroup.setText(stageName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String description = childData.get(groupData.get(groupPosition)).get(childPosition).getTitle();
        Float rating = childData.get(groupData.get(groupPosition)).get(childPosition).getRating();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_childview, null);
        }

        TextView textGroup = convertView.findViewById(R.id.description);
        textGroup.setText(description);

        TextView ratingValue = convertView.findViewById(R.id.rating);
        ratingValue.setText(String.format(Locale.US, "%.1f", (float) rating));

        RatingBar bar = convertView.findViewById(R.id.ratingBar_small);
        bar.setRating(rating);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
