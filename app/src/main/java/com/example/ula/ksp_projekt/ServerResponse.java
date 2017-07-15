package com.example.ula.ksp_projekt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ula on 12.05.2017.
 */

public class ServerResponse {


    String message;
    String token;

    public ServerResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }
}
