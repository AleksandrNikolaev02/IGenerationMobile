package com.example.igenerationmobile.interfaces;

import com.example.igenerationmobile.dto.FieldDto;
import com.example.igenerationmobile.dto.ModeDto;
import com.example.igenerationmobile.dto.TrackDto;
import com.example.igenerationmobile.dto.UserDto;
import com.example.igenerationmobile.dto.CreateProjectDto;
import com.example.igenerationmobile.fragments.myProject.ProjectDto;
import com.example.igenerationmobile.model.Field;
import com.example.igenerationmobile.model.InfoProject;
import com.example.igenerationmobile.model.Login;
import com.example.igenerationmobile.model.Project;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.Track;
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

    @POST("login")
    Call<Token> login(@Body Login loginDto);

    @POST("projects/add-project")
    Call<InfoProject> addProject(@Header("Authorization") String token, @Body CreateProjectDto createProjectDto);

    @POST("tracks")
    Call<List<Track>> tracks(@Header("Authorization") String token, @Body TrackDto trackDto);

    @POST("set-track")
    Call<Project> setTrack(@Header("Authorization") String token, @Body ProjectDto projectDto);
}
