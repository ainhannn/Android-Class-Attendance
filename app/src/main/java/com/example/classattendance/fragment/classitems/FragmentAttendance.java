package com.example.classattendance.fragment.classitems;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.api.AttendanceAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.recycler.AttendanceAdapter;
import com.example.classattendance.utils.MyAuth;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAttendance extends Fragment {
    // component
    private View rootView;
    private Button button;
    private RecyclerView recyclerView;
    private BottomSheetBehavior bottomSheetBehavior;

    // service
    private FusedLocationProviderClient fusedLocationClient;
    private AttendanceAPI api;

    // data
    private AttendanceAdapter adapter;
    private List<Attendance> data = new ArrayList<>();

    // const
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private int CURRENT_CLASS_ID;
    private String ROLE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Button create new session
        button = rootView.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // Recycler View display attendance session list
        recyclerView = rootView.findViewById(R.id.recyclerViewAttendance);
        adapter = new AttendanceAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Service
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        api = NetworkUtil.self().getRetrofit().create(AttendanceAPI.class);

        // Data
        CURRENT_CLASS_ID = getActivity().getIntent().getIntExtra("class_id", 0);
        ROLE = getActivity().getIntent().getStringExtra("role");

        if (ROLE.equalsIgnoreCase("teacher")) onCreateViewRoleTeacher();
        else onCreateViewRoleStudent();

        return rootView;
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


    private void onCreateViewRoleTeacher() {
        // Load data to Recycler View display attendance session list
        loadAdapter(api.getAttendanceRoleTeacher(CURRENT_CLASS_ID, MyAuth.getUid()));

        // Bottom Sheet create new session
        RelativeLayout createClassLayout = rootView.findViewById(R.id.create_session_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(createClassLayout);

        // EditText on Bottom Sheet
        EditText txtExpiry = rootView.findViewById(R.id.txt_time_expiry);
        EditText txtLate = rootView.findViewById(R.id.txt_time_late);

        // Set onclick event on Bottom Sheet
        Button btnCreateCode = rootView.findViewById(R.id.btn_create_code);
        btnCreateCode.setOnClickListener(v -> {
            // Check for location permission
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                createCode(txtLate.getText().toString(), txtExpiry.getText().toString());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void createCode(String strLate, String strExpiry) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("Location", "Provider: " + location.getProvider() + ", Latitude: " + latitude + ", Longitude: " + longitude);

                        // Code here
                        AttendanceCreateDTO dto = new AttendanceCreateDTO();
                        dto.setCreateTime(new Date());
                        dto.setLocation(latitude + "," + longitude);
                        dto.setExpiryAfter(Integer.valueOf(strExpiry));
                        dto.setLateAfter(Integer.valueOf(strLate));
                        dto.setClassId(CURRENT_CLASS_ID);

                        // Call API
                        Call<Attendance> call = api.createAttendance(MyAuth.getUid(), dto);
                        call.enqueue(new Callback<Attendance>() {
                            @Override
                            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                                if (response.isSuccessful()) {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    Toast.makeText(getContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                                    data.add(0, response.body());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                                }
                            }
                            @Override
                            public void onFailure(Call<Attendance> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage());
                            }
                        });
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                Toast.makeText(getActivity(), "Location permission granted\nTry action again", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onCreateViewRoleStudent() {
        // Set up for Button create new session
        button.setText("Điểm danh");

        // Load data to Recycler View display attendance session list
        loadAdapter(api.getAttendanceRoleStudent(CURRENT_CLASS_ID, MyAuth.getUid()));

        // Bottom Sheet create new session
        RelativeLayout createClassLayout = rootView.findViewById(R.id.create_session_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(createClassLayout);


    }

    private void loadAdapter(Call<List<Attendance>> call) {
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}