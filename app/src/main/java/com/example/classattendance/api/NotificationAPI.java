package com.example.classattendance.api;

import com.example.classattendance.model.Notification;
import com.example.classattendance.model.User;
import com.example.classattendance.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationAPI {
    @GET("notification/{UID}")
    Call<Notification> getNotificationByUID(@Query("UID") String uid);

}
