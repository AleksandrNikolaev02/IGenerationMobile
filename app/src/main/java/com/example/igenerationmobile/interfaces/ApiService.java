package com.example.igenerationmobile.interfaces;

import com.example.igenerationmobile.dto.FieldDto;
import com.example.igenerationmobile.model.Field;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("fields-with-content")
    Call<List<Field>> fieldsWithContent(@Header("Authorization") String token, @Body FieldDto fieldDto);
}
