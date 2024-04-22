package com.example.classattendance.fragment.classitems;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.api.AttendanceAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.fragment.FirstFragment;
import com.example.classattendance.fragment.classitems.attendanceteacher.FragmentCreateAttendance;
import com.example.classattendance.model.Attendance;
import com.example.classattendance.recycler.AttendanceAdapter;
import com.example.classattendance.utils.MyAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAttendance extends Fragment {
    private RecyclerView recyclerView;
    private Button button;
    private AttendanceAdapter adapter;
    private List<Attendance> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAttendance);
        button = view.findViewById(R.id.button);
        adapter = new AttendanceAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity().getIntent().getStringExtra("role").contains("teacher"))
            roleTeacher();
        else
            roleStudent();
    }

    private void roleTeacher() {
        button.setOnClickListener(v -> {
            // code here
            // mo trinh tao ma diem danh
        });

        AttendanceAPI api = NetworkUtil.self().getRetrofit().create(AttendanceAPI.class);
        Call<List<Attendance>> call = api.getAttendanceRoleTeacher(
                getActivity().getIntent().getIntExtra("class_id", 0),
                MyAuth.getUid());
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void roleStudent() {
        button.setText("Điểm danh");
        button.setOnClickListener(v -> {
            // code here
            // mo cua so nhap ma diem danh hoac quet ma
        });

        AttendanceAPI api = NetworkUtil.self().getRetrofit().create(AttendanceAPI.class);
        Call<List<Attendance>> call = api.getAttendanceRoleStudent(
                getActivity().getIntent().getIntExtra("class_id", 0),
                MyAuth.getUid());
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewAttendance);
            AttendanceAdapter adapter = (AttendanceAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                for (int i = 0; i < data.size(); i++) {
                    AttendanceAdapter.AttendanceViewHolder holder = (AttendanceAdapter.AttendanceViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        LinearLayout memberAttendanceLayout = holder.itemView.findViewById(R.id.memberAttendance);
                        memberAttendanceLayout.setVisibility(View.GONE);
                        RelativeLayout attendanceLayout = holder.itemView.findViewById(R.id.attendanceClicked);
                        attendanceLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}