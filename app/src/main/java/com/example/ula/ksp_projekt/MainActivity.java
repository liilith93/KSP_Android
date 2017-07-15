package com.example.ula.ksp_projekt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnConnectClick(View view) throws IOException, JSONException {

        TextView wynik = (TextView) findViewById(R.id.txtPomiar);
        EditText txtIP = (EditText) findViewById(R.id.txtIP);
        final TextView txtResp = (TextView) findViewById(R.id.txtResp);

        IP = txtIP.getText().toString();
        //String contentType = "application/json";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit rtf = new Retrofit.Builder()
                .baseUrl(IP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final IServer service = rtf.create(IServer.class);


        JSONObject userJS = new JSONObject();
        userJS.put("name", "Urszula");

        //RequestBody user = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), userJS.toString());

        Call<ServerResponse> createConn = service.connect(userJS);

        createConn.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                ServerResponse responseToken = response.body();
                String token = responseToken.token;

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable throwable) {
                throwable.printStackTrace();
                txtResp.setText(throwable.getMessage());
            }
        });

    }
}
