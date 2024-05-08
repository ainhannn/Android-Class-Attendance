package com.example.classattendance.fragment.classitems;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.SimpleUser;
import com.example.classattendance.adaptor.MemberAdapter;
import com.example.classattendance.viewmodel.ClassVM;

import java.util.ArrayList;
import java.util.List;

public class FragmentPeople extends Fragment {
    private TextView classNameTextView;
    private TextView classSubTextView;
    private TextView classStatusTextView;
    private ImageView classBackground;
    private TextView memberLength;
    private MemberAdapter memberAdapter;
    private List<SimpleUser> memberList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        // Include
        View includeView = view.findViewById(R.id.include);
        classNameTextView = includeView.findViewById(R.id.class_name);
        classSubTextView = includeView.findViewById(R.id.class_sub);
        classStatusTextView = includeView.findViewById(R.id.status);
        classBackground = includeView.findViewById(R.id.class_background);
        classNameTextView.setTextColor(Color.WHITE);
        classSubTextView.setTextColor(Color.WHITE);
        classStatusTextView.setTextColor(Color.WHITE);

        // Member List
        memberLength = view.findViewById(R.id.member_length);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPeople);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberList = new ArrayList<>();
        memberAdapter = new MemberAdapter(memberList, getContext());
        recyclerView.setAdapter(memberAdapter);

        // Load data
        ClassVM classVM = new ViewModelProvider(this).get(ClassVM.class);
        classVM.getClassById(getActivity().getIntent().getIntExtra("class_id", 0))
                .observe(getActivity(), rs -> {
                    if (rs != null) {
                        // Pass data to include
                        classNameTextView.setText(rs.getName());
                        classSubTextView.setText("Giáo viên: " + rs.getTeacher().getName());
                        classBackground.setBackgroundResource(R.drawable.member_background);
                        if (getActivity().getIntent().getStringExtra("role").contains("teacher"))
                            classStatusTextView.setText("Created");
                        else
                            classStatusTextView.setText("Joined");

                        // Pass data to people list
                        memberList.clear();
                        memberList.addAll(rs.getMembers());
                        memberAdapter.notifyDataSetChanged();
                        memberLength.setText(String.valueOf(memberAdapter.getItemCount()));
                    }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}