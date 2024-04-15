package com.example.classattendance.api;

import com.example.classattendance.model.Attendance;
import com.example.classattendance.model.AttendanceCreateDTO;
import com.example.classattendance.model.AttendanceRecord;
import com.example.classattendance.model.AttendanceTakeDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttendanceAPI {
    @POST("attendance/create")
    Call<Attendance> createAttendance(@Query("UID") String uid,
                                      @Body AttendanceCreateDTO attendanceCreateDTO);

    @POST("attendance/take")
    Call<AttendanceRecord> takeAttendance(@Query("UID") String uid,
                                          @Body AttendanceTakeDTO attendanceTakeDTO);


    @GET("attendance/{classId}/teacher")
    Call<Attendance> getAttendanceRoleTeacher(@Query("UID") String uid,
                                              @Path("classId") String classId);

    @GET("attendance/{classId}/student")
    Call<AttendanceRecord> getAttendanceRoleStudent(@Query("UID") String uid,
                                                    @Path("classId") String classId);

    @DELETE("attendance/{attendanceId}/delete")
    Call<Void> deleteAttendance(@Query("UID") String uid,
                                @Path("attendanceId") String attendanceId);

}
