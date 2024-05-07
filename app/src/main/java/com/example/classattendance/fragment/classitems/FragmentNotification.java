package com.example.classattendance.fragment.classitems;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.Notification;
import com.example.classattendance.adaptor.NotificationAdapter;
import com.example.classattendance.utils.MyAuth;
import com.example.classattendance.viewmodel.ClassVM;
import com.example.classattendance.viewmodel.NotificationVM;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotification extends Fragment {

    private TextView classNameTextView;
    private TextView classSubTextView;
    private TextView classStatusTextView;
    private ImageView classBackground;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private NotificationVM notificationVM;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationVM = new ViewModelProvider(this).get(NotificationVM.class);

        // Include
        View includeView = view.findViewById(R.id.include);
        classNameTextView = includeView.findViewById(R.id.class_name);
        classSubTextView = includeView.findViewById(R.id.class_sub);
        classStatusTextView = includeView.findViewById(R.id.status);
        classBackground = includeView.findViewById(R.id.class_background);
        classBackground.setBackgroundResource(R.drawable.noti_background);

        // Send notification
        ShapeableImageView avt = view.findViewById(R.id.user);
        EditText editText = view.findViewById(R.id.editText);
        ImageView iconSend = view.findViewById(R.id.iconSend);
        if (MyAuth.getCurrentUser() != null) {
            avt.setImageURI(MyAuth.getCurrentUser().getPhotoUrl());
        }
        iconSend.setOnClickListener(v -> {
            String content = editText.getText().toString();
            if (!content.isEmpty()) {
                notificationVM.createNotification(
                        getActivity().getIntent().getIntExtra("class_id", 0),
                        content)
                    .observe(getActivity(), rs -> {
                        if (rs != null) {
                            editText.setText(null);
                            editText.clearFocus();

                            InputMethodManager imm = getSystemService(getContext(), InputMethodManager.class);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            notificationList.add(rs);
                            notificationAdapter.notifyDataSetChanged();
                        }
                    });
            }
        });

        // Notification List
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        recyclerView.setAdapter(notificationAdapter);


        // Load data
        ClassVM classVM = new ViewModelProvider(this).get(ClassVM.class);
        classVM.getClassById(getActivity().getIntent().getIntExtra("class_id", 0))
                .observe(getActivity(), rs -> {
                    if (rs != null) {
                        Class classItem = rs;

                        // Pass data to include
                        classNameTextView.setText(classItem.getName());
                        classSubTextView.setText("Giáo viên: " + classItem.getTeacher().getName());
                        if (getActivity().getIntent().getStringExtra("role").contains("teacher"))
                            classStatusTextView.setText("Created");
                        else
                            classStatusTextView.setText("Joined");

                        // Pass data to notification list
                        notificationList.clear();
                        notificationList.addAll(classItem.getNotifications());
                        notificationAdapter.notifyDataSetChanged();
                    }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationVM.getNotificationByClassId(getActivity().getIntent().getIntExtra("class_id", 0)).observe(getActivity(), rs -> {
            // Pass data to notification list
            notificationList.clear();
            notificationList.addAll(rs);
            notificationAdapter.notifyDataSetChanged();
        });
    }
}