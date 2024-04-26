package com.example.classattendance.fragment.classitems;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.api.IClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.api.INotificationAPI;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.Notification;
import com.example.classattendance.adaptor.NotificationAdapter;
import com.example.classattendance.utils.MyAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotification extends Fragment {

    private TextView classNameTextView;
    private TextView classSubTextView;
    private TextView classStatusTextView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Include
        View includeView = view.findViewById(R.id.include);
        classNameTextView = includeView.findViewById(R.id.class_name);
        classSubTextView = includeView.findViewById(R.id.class_sub);
        classStatusTextView = includeView.findViewById(R.id.status);

        // Send notification
        EditText editText = view.findViewById(R.id.editText);
        ImageView iconSend = view.findViewById(R.id.iconSend);
        iconSend.setOnClickListener(v -> {
            String content = editText.getText().toString();
            if (!content.isEmpty()) {
                INotificationAPI api = NetworkUtil.self().getRetrofit().create(INotificationAPI.class);
                Call<Notification> call = api.createNotification(
                        getActivity().getIntent().getIntExtra("class_id", 0),
                        MyAuth.getUid(),
                        content);
                call.enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        if (response.isSuccessful()) {
                            editText.setText(null);
                            editText.clearFocus();

                            InputMethodManager imm = getSystemService(getContext(), InputMethodManager.class);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            notificationList.add(0,response.body());
                            notificationAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "onResponse: " + response.errorBody().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
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
        IClassAPI api = NetworkUtil.self().getRetrofit().create(IClassAPI.class);
        Call<Class> call = api.getClassById(getActivity().getIntent().getIntExtra("class_id", 0));
        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class classItem = response.body();

                    // Pass data to include
                    classNameTextView.setText(classItem.getName());
                    classSubTextView.setText(classItem.getTeacher().getName());
                    if (getActivity().getIntent().getStringExtra("role").contains("teacher"))
                        classStatusTextView.setText("Created");
                    else
                        classStatusTextView.setText("Joined");

                    // Pass data to notification list
                    notificationList.clear();
                    notificationList.addAll(classItem.getNotifications());
                    notificationAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        INotificationAPI api = NetworkUtil.self().getRetrofit().create(INotificationAPI.class);
        Call<List<Notification>> call = api.getNotificationByClassId(getActivity().getIntent().getIntExtra("class_id", 0));
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    // Pass data to notification list
                    notificationList.clear();
                    notificationList.addAll(response.body());
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}