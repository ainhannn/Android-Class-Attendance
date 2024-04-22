package com.example.classattendance.api;

import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationAPI {
    @GET("notification/{UID}/all")
    Call<List<Notification>> getNotificationByUID(@Query("UID") String uid);

    @GET("notification/{classId}")
    Call<List<Notification>> getNotificationByClassId(@Query("ClassId") int classId);

    @POST("notification/create")
    Call<Notification> createNotification(@Query("classId") int classId,
                                          @Query("UID") String uid,
                                          @Query("content") String content);
}