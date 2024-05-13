package com.example.igenerationmobile.interfaces;

import com.example.igenerationmobile.dto.FieldDto;
import com.example.igenerationmobile.dto.ModeDto;
import com.example.igenerationmobile.dto.UserDto;
import com.example.igenerationmobile.model.Field;
import com.example.igenerationmobile.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ApiService {
    @POST("fields-with-content")
    Call<List<Field>> fieldsWithContent(@Header("Authorization") String token, @Body FieldDto fieldDto);

    @POST("user")
    Call<User> user(@Header("Authorization") String token, @Body UserDto userDto);

    @PATCH("profile/update")
    Call<Void> update(@Header("Authorization") String token, @Body ModeDto modeDto);
}
