package com.example.classattendance.api;

import com.example.classattendance.model.User;
import com.example.classattendance.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserAPI {
    @GET("user/login")
    Call<User> login(
            @Query("UID") String uid);

    @POST("user/register")
    Call<User> register(
            @Body UserDTO dto);

    @PUT("user/{UID}/update")
    Call<Void> update(
            @Path("UID") String uid,
            @Query("name") String name);
}
