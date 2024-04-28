package com.example.classattendance.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.classattendance.api.IClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.ClassDTO;
import com.example.classattendance.utils.MyAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassVM extends ViewModel {
    private IClassAPI api;

    public ClassVM() {
        api = NetworkUtil.self().getRetrofit().create(IClassAPI.class);
    }

    public LiveData<Class> getClassById(int id) {
        MutableLiveData<Class> result = new MutableLiveData<>();
        result.setValue(null);
        api.getClassById(id).enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<String> archiveClass(int id) {
        MutableLiveData<String> result = new MutableLiveData<>();
        result.setValue("failed");

        Call<Void> call = api.archiveClass(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue("successful");
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<String> outClass(int classId, int userId) {
        MutableLiveData<String> result = new MutableLiveData<>();
        result.setValue("failed");

        Call<Void> call = api.outClass(classId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue("successful");
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<Class> createClass(ClassDTO dto) {
        MutableLiveData<Class> result = new MutableLiveData<>();
        result.setValue(null);

        Call<Class> call = api.createClass(MyAuth.getUid(), dto);
        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<Class> joinClass(String code) {
        MutableLiveData<Class> result = new MutableLiveData<>();
        result.setValue(null);

        Call<Class> call = api.joinClass(MyAuth.getUid(), code);
        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.e(TAG, "onResponse: successful");
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

}
