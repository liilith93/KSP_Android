package com.example.ula.ksp_projekt;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by Ula on 12.05.2017.
 */

public interface IServer {

    @Headers("Content-Type: application/json")
    @POST("/connect/")
    Call<ServerResponse> connect( @Body User user);

    @POST("/disconnect")
    Call<ServerResponse> disconnect(@Header("Token") String token);

    @GET("/measurements")
    Call<Measurment> measurements(@Header("Token") String token);
}
