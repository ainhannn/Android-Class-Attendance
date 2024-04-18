package com.example.classattendance.fragment.classitems;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.Notification;
import com.example.classattendance.recycler.NotificationAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentNotification extends Fragment {

    private static final String ARG_CLASS_NAME = "className";
    private static final String ARG_CLASS_SUBJECT = "classSubject";

    private String className;
    private String classSubject;

    public FragmentNotification() {
        // Required empty public constructor
    }

    public static FragmentNotification newInstance(String className, String classSubject) {
        FragmentNotification fragment = new FragmentNotification();
        Bundle args = new Bundle();
        args.putString(ARG_CLASS_NAME, className);
        args.putString(ARG_CLASS_SUBJECT, classSubject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            className = getArguments().getString(ARG_CLASS_NAME);
            classSubject = getArguments().getString(ARG_CLASS_SUBJECT);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Pass data to include
        View includeView = view.findViewById(R.id.include);
        TextView classNameTextView = includeView.findViewById(R.id.class_name);
        TextView classSubTextView = includeView.findViewById(R.id.class_sub);
        classNameTextView.setText(className);
        classSubTextView.setText(classSubject);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Notification> notificationList = new ArrayList<>();
        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList, getContext());
        recyclerView.setAdapter(notificationAdapter);

        notificationList.add(new Notification(new Date(), "Ngô Lê Huệ Ngân", "Đây là thông báo mẫu ahihi"));
        notificationList.add(new Notification(new Date(), "John Doe", "Nội dung thông báo 2"));

        notificationAdapter.notifyDataSetChanged();

        return view;
    }
}