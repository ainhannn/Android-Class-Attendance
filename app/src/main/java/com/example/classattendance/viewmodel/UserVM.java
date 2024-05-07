package com.example.classattendance.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.classattendance.api.IUserAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.User;
import com.example.classattendance.model.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserVM extends ViewModel {
    private IUserAPI api;

    public UserVM() {
        api = NetworkUtil.self().getRetrofit().create(IUserAPI.class);
    }

    public LiveData<User> login(String uid) {
        MutableLiveData<User> result = new MutableLiveData<>();
        Call<User> call = api.login(uid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    result.setValue(null);
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<User> register(UserDTO dto) {
        MutableLiveData<User> result = new MutableLiveData<>();
        Call<User> call = api.register(dto);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    result.setValue(null);
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }
}
