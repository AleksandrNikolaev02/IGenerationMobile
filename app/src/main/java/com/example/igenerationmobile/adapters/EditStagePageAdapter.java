package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.model.ExpandableListModel.Task;
import com.example.igenerationmobile.model.ExpandableListModel.Title;

import java.util.List;
import java.util.Map;

public class EditStagePageAdapter extends BaseExpandableListAdapter {

    private Context context;
    public List<Title> groupData;
    public Map<String, List<Task>> childData;

    public EditStagePageAdapter(Context context,
                         List<Title> groupData,
                         Map<String, List<Task>> childData) {
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
        return childData.get(groupData.get(groupPosition).getTitle()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childData.get(groupData.get(i).getTitle()).get(i1);
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
    public int getChildType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildTypeCount() {
        return 1;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Title title = (Title) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_groupview, null);
        }

        TextView textGroup = convertView.findViewById(R.id.question);
        textGroup.setText(title.getTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int type = getChildType(groupPosition, childPosition);
        Task stage = childData.get(groupData.get(groupPosition).getTitle()).get(childPosition);

        String titleStr = stage.getTitle();
        String taskStr = stage.getTask();

        if (convertView == null || (Integer)convertView.getTag() != type) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (type) {
                case 0:
                    convertView = inflater.inflate(R.layout.task_view, null);
                    break;
            }
            convertView.setTag(type);
        }

        WebView title = convertView.findViewById(R.id.title);
        WebView task = convertView.findViewById(R.id.task);

        title.loadDataWithBaseURL(null, titleStr, "text/html", "UTF-8", null);
        task.loadDataWithBaseURL(null, taskStr, "text/html", "UTF-8", null);

        title.setBackgroundColor(context.getColor(R.color.title));
        task.setBackgroundColor(context.getColor(R.color.task));

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

