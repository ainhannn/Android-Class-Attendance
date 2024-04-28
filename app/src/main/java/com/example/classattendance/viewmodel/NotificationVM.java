package com.example.classattendance.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.classattendance.api.INotificationAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Notification;
import com.example.classattendance.utils.MyAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationVM extends ViewModel {
    private INotificationAPI api;

    public NotificationVM() {
        api = NetworkUtil.self().getRetrofit().create(INotificationAPI.class);
    }

    public LiveData<Notification> createNotification(int classId, String content) {
        MutableLiveData<Notification> result = new MutableLiveData<>();
        result.setValue(null);
        Call<Notification> call = api.createNotification(classId, MyAuth.getUid(),content);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<List<Notification>> getNotificationByClassId(int classId) {
        MutableLiveData<List<Notification>> result = new MutableLiveData<>();
        result.setValue(new ArrayList<>());

        INotificationAPI api = NetworkUtil.self().getRetrofit().create(INotificationAPI.class);
        Call<List<Notification>> call = api.getNotificationByClassId(classId);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }
}
