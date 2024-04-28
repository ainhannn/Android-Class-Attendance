package com.example.classattendance.fragment.classitems;

import static android.app.Activity.RESULT_OK;

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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.activity.ScanQRActivity;
import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.AttendanceTakeDTO;
import com.example.classattendance.adaptor.AttendanceAdapter;
import com.example.classattendance.utils.Valid;
import com.example.classattendance.viewmodel.AttendanceVM;
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

public class FragmentAttendance extends Fragment {
    // component
    private View rootView;
    private BarChart barChart;
    private PieChart pieChart;
    private Button button;
    private RecyclerView recyclerView;
    private BottomSheetBehavior bottomSheetBehavior;
    private EditText txtCode;

    // service
    private AttendanceVM attendanceVM;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationCallback locationCallback;

    // data
    private AttendanceAdapter adapter;
    private List<Attendance> data = new ArrayList<>();
    private boolean locationReady = false;

    // const
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_SCAN_QR = 2;
    private int CURRENT_CLASS_ID;
    private String ROLE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Data
        CURRENT_CLASS_ID = getActivity().getIntent().getIntExtra("class_id", 0);
        ROLE = getActivity().getIntent().getStringExtra("role");

        // Service
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        attendanceVM = new ViewModelProvider(this).get(AttendanceVM.class);

        // RootView
        rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Chart
        barChart = rootView.findViewById(R.id.bar_chart);
        pieChart = rootView.findViewById(R.id.pie_chart);

        // Button create new session
        button = rootView.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // EditText get code from scanner
        txtCode = rootView.findViewById(R.id.txt_code);

        // Recycler View display attendance session list
        recyclerView = rootView.findViewById(R.id.recyclerViewAttendance);
        adapter = new AttendanceAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        loadAdapter(attendanceVM.getAttendance(CURRENT_CLASS_ID, ROLE));

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
        // Bottom Sheet create new session
        RelativeLayout layout = rootView.findViewById(R.id.create_session_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        // EditText on Bottom Sheet
        EditText txtExpiry = rootView.findViewById(R.id.txt_time_expiry);
        EditText txtLate = rootView.findViewById(R.id.txt_time_late);

        // Set onclick event on Bottom Sheet
        Button btnCreateCode = rootView.findViewById(R.id.btn_create_code);
        btnCreateCode.setOnClickListener(v -> {
            if (Valid.isInteger(String.valueOf(txtExpiry.getText()))
                    && (txtLate.getText() == null || Valid.isInteger(String.valueOf(txtLate.getText()))))
                createCode(txtLate.getText().toString(), txtExpiry.getText().toString());
            else
                Toast.makeText(getContext(), "Vui lòng nhập mã hợp lệ", Toast.LENGTH_SHORT).show();
        });
    }
    private void onCreateViewRoleStudent() {
        // Set up for Button create new session
        button.setText("Điểm danh");

        // Bottom Sheet create new session
        RelativeLayout layout = rootView.findViewById(R.id.take_attendance_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        // Scan QR button
        ImageView btnScan = rootView.findViewById(R.id.btn_scan_qr);
        btnScan.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ScanQRActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
        });

        // Set onclick event on Bottom Sheet
        Button btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            if (Valid.isCode(String.valueOf(txtCode.getText())))
                takeAttendance(String.valueOf(txtCode.getText()));
            else
                Toast.makeText(getContext(), "Vui lòng nhập mã hợp lệ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("result");
            if (result != null) {
                txtCode.setText(result);
            }
        }
    }

    // Load data into adaptor
    private void loadAdapter(LiveData<List<Attendance>> listLiveData) {
        listLiveData.observe(getActivity(), rs -> {
            data.clear();
            data.addAll(rs);
            adapter.notifyDataSetChanged();

            if (ROLE.equalsIgnoreCase("teacher"))
                loadBarChart();
            else
                loadPieChart();
        });
    }
    private void loadBarChart() {
        if (data.size() == 0) return;

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
        barChart.setVisibility(View.VISIBLE);
    }
    private void loadPieChart() {
        if (data.size() == 0) return;

        float onTime = 0;
        float late = 0;
        for (Attendance a : data) {
            onTime += a.getPresentCount();
            late += a.getLateCount();
        }
        onTime = onTime/ data.size();
        late = late/ data.size();

        // Tạo dữ liệu cho PieChart
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(onTime, "On time"));
        entries.add(new PieEntry(late, "Late"));
        entries.add(new PieEntry(1f - onTime - late, ""));

        // Tạo PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");

        // Cấu hình màu sắc cho các phần
        dataSet.setColors(Color.GREEN, Color.YELLOW, Color.TRANSPARENT); // Màu trong suốt cho phần trống

        // Tạo PieData từ PieDataSet
        PieData pieData = new PieData(dataSet);

        // Đặt dữ liệu cho PieChart
        pieChart.setData(pieData);

        // Hiển thị PieChart
        pieChart.invalidate();
        pieChart.setVisibility(View.VISIBLE);
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

                    attendanceVM.createAttendance(dto).observe(getActivity(), rs -> {
                        if (rs != null) {
                            Toast.makeText(getContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            data.add(0, rs);
                            adapter.notifyDataSetChanged();
                        }
                    });

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

                    attendanceVM.takeAttendance(dto).observe(getActivity(), rs -> {
                        if (rs != null) {
                            Toast.makeText(getContext(), "Điểm danh thành công", Toast.LENGTH_SHORT).show();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            data.add(0, rs);
                            adapter.notifyDataSetChanged();
                        }
                    });

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