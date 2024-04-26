package com.example.classattendance.api;

import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.AttendanceTakeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAttendanceAPI {
    @POST("attendance/create")
    Call<Attendance> createAttendance(@Query("UID") String uid,
                                      @Body AttendanceCreateDTO attendanceCreateDTO);

    @POST("attendance/take")
    Call<Attendance> takeAttendance(@Query("UID") String uid,
                                          @Body AttendanceTakeDTO attendanceTakeDTO);


    @GET("attendance/{classId}/teacher")
    Call<List<Attendance>> getAttendanceRoleTeacher(@Path("classId") int classId,
                                                    @Query("UID") String uid);

    @GET("attendance/{classId}/student")
    Call<List<Attendance>> getAttendanceRoleStudent(@Path("classId") int classId,
                                                          @Query("UID") String uid);

    @DELETE("attendance/{attendanceId}/delete")
    Call<Void> deleteAttendance(@Path("attendanceId") String attendanceId,
                                @Query("UID") String uid);

}
