package com.example.ula.ksp_projekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Ula on 05.07.2017.
 */

public class SocketClient {

    private String hostname;
    private int port;
    Socket socketClient;

    public SocketClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException {
        System.out.println("Attempting to connect to " + hostname + ":" + port);
        socketClient = new Socket(hostname, port);
        System.out.println("Connection Established");
    }

    public String readResponse() throws IOException {
        String userInput;
        String returnedMessage = "";
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

        while ((userInput = stdIn.readLine()) != null) {
            returnedMessage += userInput;
        }

        socketClient.close();

        return returnedMessage;
    }

    public void sendMessageToEncryption(String messageToEncryption) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        writer.write(messageToEncryption + "\n");
        writer.write("ENDTRANSFER");
        writer.newLine();
        writer.flush();
    }
}
