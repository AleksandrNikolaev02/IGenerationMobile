package com.example.igenerationmobile.fragments.myProject;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.igenerationmobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmptyProject extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    public EmptyProject() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_empty_project, container, false);

        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView2);

            toolbar = getActivity().findViewById(R.id.toolbar);

            MenuItem trajectory = bottomNavigationView.getMenu().findItem(R.id.trajectoryProject);

            toolbar.setTitle(trajectory.getTitle());
        }

        return view;
    }
}