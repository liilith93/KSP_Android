package com.example.ula.ksp_projekt;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Ula on 05.07.2017.
 */

public class ServerConnection {

    public String connectWithServer(String messageToSend) {

        SocketClient client = new SocketClient("localhost", 8080);
        String encryptedMessage = null;
        try {
            client.connect();
            client.sendMessageToEncryption(messageToSend);
            encryptedMessage = client.readResponse();

        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up." + e.getMessage());
        }

        return encryptedMessage;
    }
}
