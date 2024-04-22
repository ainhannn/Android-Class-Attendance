package com.example.classattendance.fragment.classitems.attendanceteacher;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classattendance.R;
import com.example.classattendance.api.AttendanceAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.api.UserAPI;
import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreateAttendance extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_attendance, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // EditText
        EditText txtExpiry = rootView.findViewById(R.id.txt_time_expiry);
        EditText txtLate = rootView.findViewById(R.id.txt_time_late);

        // Set onclick event
        Button btnCreateCode = rootView.findViewById(R.id.btn_create_code);
        btnCreateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission already granted
                    createCode(txtLate.getText().toString(), txtExpiry.getText().toString());
                }
            }
        });

        return rootView;
    }

    @SuppressLint("MissingPermission")
    private void createCode(String strLate, String strExpiry) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
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
                            dto.setClassId(1);

                            // Call API
                            AttendanceAPI api = NetworkUtil.self().getRetrofit().create(AttendanceAPI.class);
                            Call<Attendance> call = api.createAttendance("hMnuzMB8SdMPHxaYMD8ooQHcrXB2", dto);
                            call.enqueue(new Callback<Attendance>() {
                                @Override
                                public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                                    if (response.isSuccessful()) {
                                        Attendance attendance = response.body();
                                        Log.d(TAG, "onResponse: " + attendance.getCode());

//                                        if (attendance != null) {
//                                            data.clear();
//                                            data.addAll(user.getCreatedClasses());
//                                            adapter.notifyDataSetChanged();
//                                        }

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
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                Toast.makeText(getActivity(), "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}