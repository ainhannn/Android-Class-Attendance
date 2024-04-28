package com.example.classattendance.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.classattendance.api.IAttendanceAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.AttendanceTakeDTO;
import com.example.classattendance.utils.MyAuth;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceVM extends ViewModel {
    private IAttendanceAPI api;

    public AttendanceVM() {
        api = NetworkUtil.self().getRetrofit().create(IAttendanceAPI.class);
    }

    public LiveData<List<Attendance>> getAttendance(int classId, String role) {
        MutableLiveData<List<Attendance>> result = new MutableLiveData<>();
        result.setValue(new ArrayList<>());

        Call<List<Attendance>> call = null;
        if (role.equalsIgnoreCase("teacher")) {
            call = api.getAttendanceRoleTeacher(classId, MyAuth.getUid());
        } else {
            call = api.getAttendanceRoleStudent(classId, MyAuth.getUid());
        }

        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return result;
    }

    public LiveData<Attendance> createAttendance(AttendanceCreateDTO dto) {
        MutableLiveData<Attendance> result = new MutableLiveData<>();
        result.setValue(null);

        Call<Attendance> call = api.createAttendance(MyAuth.getUid(), dto);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<Attendance> takeAttendance(AttendanceTakeDTO dto) {
        MutableLiveData<Attendance> result = new MutableLiveData<>();
        result.setValue(null);

        Call<Attendance> call = api.takeAttendance(MyAuth.getUid(), dto);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

}
