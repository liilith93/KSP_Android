package com.example.ula.ksp_projekt;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String IP;
    String token = "null";
    Retrofit rtf;
    IServer service;
    TextView txtResp;
    Button connect;
    Button disconnect;
    EditText txtName;
    EditText txtIP;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnConnectClick(View view) throws IOException, JSONException {

        txtName = (EditText) findViewById(R.id.txtName);
        txtIP = (EditText) findViewById(R.id.txtIP);
        txtResp = (TextView) findViewById(R.id.txtResp);
        disconnect = (Button) findViewById(R.id.btnDisconnect);
        connect = (Button) findViewById(R.id.btnConnect);

        IP = txtIP.getText().toString();
        User userObj = new User(txtName.getText().toString());
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            rtf = new Retrofit.Builder()
                    .baseUrl(IP)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service = rtf.create(IServer.class);

            Call<ServerResponse> createConn = service.connect(userObj);
            createConn.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if(response.code()==200) {
                        ServerResponse callback = response.body();
                        token = callback.token;


                        callAsyncTask();
                    }
                    else if(response.code() == 400){
                        txtResp.setText("Podaj parametry połączenia!");
                        timer.cancel();
                        timer.purge();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable throwable) {
                    throwable.printStackTrace();
                    txtResp.setText("Problem z połączeniem! \n" + throwable.getMessage());
                    timer.cancel();
                    timer.purge();
                }
            });
        }
        catch(Exception e){
            txtResp.setText("Niepoprawne dane połączenia!");
            timer.cancel();
            timer.purge();
        }
    }

    public void btnDisconnectClick(View view) {
        final Call<ServerResponse> disconnected = service.disconnect(token);
        disconnected.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.code() == 200){
                    txtResp.setText("Rozłączono!");
                    disconnect.setEnabled(false);
                    connect.setEnabled(true);
                    txtName.setText("");
                    txtIP.setText("http://");
                    timer.cancel();
                    timer.purge();
                }
                else
                {
                    txtResp.setText("Błąd podczas rozłączania!");
                    disconnect.setEnabled(false);
                    connect.setEnabled(true);
                    txtName.setText("");
                    txtIP.setText("http://");
                    timer.cancel();
                    timer.purge();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable throwable) {
                txtResp.setText("Błąd podczas rozłączania!");
                disconnect.setEnabled(false);
                connect.setEnabled(true);
                txtName.setText("");
                txtIP.setText("http://");
                timer.cancel();
                timer.purge();
            }
        });
    }

    public void callAsyncTask(){
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsyncTask = new TimerTask(){

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Call<Measurment> measure = service.measurements(token);
                            measure.enqueue(new Callback<Measurment>() {
                                @Override
                                public void onResponse(Call<Measurment> call, Response<Measurment> response) {
                                    if (response.code() == 200) {
                                        Measurment pomiar = response.body();
                                        int temperature = pomiar.temperature;
                                        int humidity = pomiar.humidity;
                                        String update = pomiar.lastUpdate;
                                        txtResp.setText("Temperatura: " + String.valueOf(temperature) + "\n" +
                                                "Wilgotność: " + String.valueOf(humidity) + "\n" +
                                                "Ostatnia aktualizacja: " + update);
                                        disconnect.setEnabled(true);
                                        connect.setEnabled(false);
                                    } else if (response.code() == 401) {
                                        txtResp.setText("Brak autoryzacji, podaj imię i połącz się ponownie z serwerem!");
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Measurment> call, Throwable throwable) {
                                    txtResp.setText("Problem z pobraniem danych! \n" + throwable.getMessage());
                                    connect.setEnabled(true);
                                    disconnect.setEnabled(false);
                                    timer.cancel();
                                    timer.purge();
                                }
                            });
                        } catch(Exception e){
                        }
                    }
                });
            }
        };
        timer.schedule(doAsyncTask, 0, 10000);

    }
}


