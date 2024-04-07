package com.example.classattendance.api;

import com.example.classattendance.model.ClassDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClassAPI {
    @POST("class/create")
    Call<Class> createClass(@Query("UID") String uid,
                            @Body ClassDTO classDTO);

    @POST("class/join")
    Call<Class> joinClass(@Query("UID") String uid,
                          @Query("classCode") String classCode);

    @GET("class/{classId}")
    Call<Class> getClassById(@Path("classId") String classId);

    @PUT("class/{classId}/update")
    Call<Void> updateClass(@Path("classId") String classId,
                           @Body ClassDTO classDTO);

    @PUT("class/{classId}/archive")
    Call<Void> archiveClass(@Path("classId") String classId);

    @PUT("class/{classId}/restore")
    Call<Void> restoreClass(@Path("classId") String classId);

    @DELETE("class/{classId}/delete")
    Call<Void> deleteClass(@Path("classId") String classId);

    @DELETE("class/{classId}/out")
    Call<Void> outClass(@Path("userId") String userId,
                        @Path("classId") String classId);

}
