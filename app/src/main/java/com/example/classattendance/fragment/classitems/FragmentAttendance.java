package com.example.classattendance.fragment.classitems;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.classattendance.model.AttendanceTakeDTO;
import com.example.classattendance.recycler.AttendanceAdapter;
import com.example.classattendance.utils.MyAuth;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAttendance extends Fragment {
    // component
    private View rootView;
    private BarChart barChart;
    private PieChart pieChart;
    private Button button;
    private RecyclerView recyclerView;
    private BottomSheetBehavior bottomSheetBehavior;

    // service
    private AttendanceAPI api;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationCallback locationCallback;

    // data
    private AttendanceAdapter adapter;
    private List<Attendance> data = new ArrayList<>();
    private boolean locationReady;

    // const
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private int CURRENT_CLASS_ID;
    private String ROLE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Data
        CURRENT_CLASS_ID = getActivity().getIntent().getIntExtra("class_id", 0);
        ROLE = getActivity().getIntent().getStringExtra("role");

        // RootView
        rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Chart
        BarChart barChart = rootView.findViewById(R.id.bar_chart);
        PieChart pieChart = rootView.findViewById(R.id.pie_chart);

        // Button create new session
        button = rootView.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
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
        // Chart
        barChart = rootView.findViewById(R.id.bar_chart);
        barChart.setVisibility(View.VISIBLE);

        // Load data to Recycler View display attendance session list
        loadAdapter(api.getAttendanceRoleTeacher(CURRENT_CLASS_ID, MyAuth.getUid()));

        // Bottom Sheet create new session
        RelativeLayout layout = rootView.findViewById(R.id.create_session_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        // EditText on Bottom Sheet
        EditText txtExpiry = rootView.findViewById(R.id.txt_time_expiry);
        EditText txtLate = rootView.findViewById(R.id.txt_time_late);

        // Set onclick event on Bottom Sheet
        Button btnCreateCode = rootView.findViewById(R.id.btn_create_code);
        btnCreateCode.setOnClickListener(v -> {
            createCode(txtLate.getText().toString(), txtExpiry.getText().toString());
        });
    }
    private void onCreateViewRoleStudent() {
        // Chart
        pieChart = rootView.findViewById(R.id.pie_chart);
        pieChart.setVisibility(View.VISIBLE);

        // Set up for Button create new session
        button.setText("Điểm danh");

        // Load data to Recycler View display attendance session list
        loadAdapter(api.getAttendanceRoleStudent(CURRENT_CLASS_ID, MyAuth.getUid()));

        // Bottom Sheet create new session
        RelativeLayout layout = rootView.findViewById(R.id.take_attendance_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        // EditText on Bottom Sheet
        EditText txtCode = rootView.findViewById(R.id.txt_code);
        ImageView btnScan = rootView.findViewById(R.id.btn_scan_qr);

        // Set onclick event on Bottom Sheet
        Button btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            takeAttendance(txtCode.getText().toString());
        });
    }

    // Load data into adaptor
    private void loadAdapter(Call<List<Attendance>> call) {
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (ROLE.equalsIgnoreCase("teacher"))
                        loadBarChart();
                    else
                        loadPieChart();
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
    private void loadBarChart() {
        // Tạo một BarChart từ XML hoặc trong code
        BarChart barChart = rootView.findViewById(R.id.bar_chart);

        // Tạo dữ liệu cho biểu đồ
        List<BarEntry> entries1 = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();

        Collections.reverse(data);
        int i = 0;
        for (Attendance a : data) {
            entries1.add(new BarEntry(++i, a.getPresentCount()));
            entries2.add(new BarEntry(i, a.getLateCount()));
        }

        // Tạo các BarDataSet cho màu xanh và màu vàng
        BarDataSet dataSet1 = new BarDataSet(entries1, "On time");
        dataSet1.setColor(Color.GREEN);
        BarDataSet dataSet2 = new BarDataSet(entries2, "Late");
        dataSet2.setColor(Color.YELLOW);

        // Chồng các BarDataSet lên nhau để tạo thành cột chồng
        BarData barData = new BarData(dataSet1, dataSet2);
        barChart.setData(barData);

        // Cấu hình biểu đồ
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);

        // Tuỳ chỉnh trục y để hiển thị số nguyên
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.mAxisMinimum = 0;
        leftAxis.setGranularity(1);

        // Tuỳ chỉnh trục x
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt trục x ở dưới biểu đồ
        xAxis.setDrawGridLines(false); // Tắt đường kẻ lưới trên trục x
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Buổi " + (int) value;
            }
        });

        // Hiển thị biểu đồ
        barChart.invalidate();
    }
    private void loadPieChart() {
        if (data.size() == 0) return;

        float onTime = 0;
        float late = 0;
        for (Attendance a : data) {
            onTime += a.getPresentCount();
            late += a.getLateCount();
        }

        // Tạo dữ liệu cho PieChart
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(onTime/data.size(), "On time"));
        entries.add(new PieEntry(late/data.size(), "Late"));
        entries.add(new PieEntry(0f, ""));

        // Tạo PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "Biểu đồ tròn");

        // Cấu hình màu sắc cho các phần
        dataSet.setColors(Color.GREEN, Color.YELLOW, Color.TRANSPARENT); // Màu trong suốt cho phần trống

        // Tạo PieData từ PieDataSet
        PieData pieData = new PieData(dataSet);

        // Đặt dữ liệu cho PieChart
        pieChart.setData(pieData);

        // Hiển thị PieChart
        pieChart.invalidate();
    }


    // Create code
    private void createCode(String strLate, String strExpiry) {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Logic to handle location object
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);

                    AttendanceCreateDTO dto = new AttendanceCreateDTO();
                    dto.setCreateTime(new Date());
                    dto.setLocation(latitude + "," + longitude);
                    dto.setExpiryAfter(Integer.valueOf(strExpiry));
                    dto.setLateAfter(Integer.valueOf(strLate));
                    dto.setClassId(CURRENT_CLASS_ID);

                    createCodeCallAPI(dto);

                    stopLocationUpdates();
                }
            }
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        getLocation();
    }
    private void createCodeCallAPI(AttendanceCreateDTO dto) {
        Call<Attendance> call = api.createAttendance(MyAuth.getUid(), dto);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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


    // Take attendance
    private void takeAttendance(String strCode) {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Logic to handle location object
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);


                    AttendanceTakeDTO dto = new AttendanceTakeDTO();
                    dto.setTime(new Date());
                    dto.setLocation(latitude + "," + longitude);
                    dto.setCode(strCode);

                    takeAttendanceCallAPI(dto);

                    stopLocationUpdates();
                }
            }
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        getLocation();
    }
    private void takeAttendanceCallAPI(AttendanceTakeDTO dto) {
        Call<Attendance> call = api.takeAttendance(MyAuth.getUid(), dto);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Điểm danh thành công", Toast.LENGTH_SHORT).show();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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


    // Service Location
    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (!locationReady) {
            ensureLocationReady();
            Toast.makeText(getContext(),"Thử lại sau khi bật vị trí", Toast.LENGTH_LONG).show();
        } else {
            try {
                fusedLocationClient.requestLocationUpdates(LocationRequest.create()
                                .setInterval(5000)
                                .setFastestInterval(500)
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setMaxWaitTime(100),
                        locationCallback, Looper.myLooper()
                );
                Toast.makeText(getContext(),"Vui lòng chờ...", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener(task -> {
                        Log.d("task", task.toString());
                    });
        }
    }
    public void ensureLocationReady() {
        locationReady = false;

        // Permission check
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Setting check
            LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                    .addAllLocationRequests(Collections.singleton(LocationRequest.create()
                            .setInterval(5000)
                            .setFastestInterval(500)
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setMaxWaitTime(100)))
                    .build();

            settingsClient = LocationServices.getSettingsClient(getContext());
            settingsClient.checkLocationSettings(request)
                    .addOnFailureListener(e -> {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                        Log.d("err", e.toString());
                    })
                    .addOnSuccessListener(locationSettingsResponse -> {
                        Log.d("Location", "location setting ok");
                        locationReady = true;
                    });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                Log.d("Location", "Location permission succeeded");
                ensureLocationReady();
            } else {
                // Permission denied, show a message or handle accordingly
                Log.d("Location", "Location permission denied");
                Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}