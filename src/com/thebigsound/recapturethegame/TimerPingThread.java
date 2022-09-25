package com.thebigsound.recapturethegame;

import org.json.simple.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class TimerPingThread extends Thread {
    private int port;

    public TimerPingThread(int port) {
        this.port = port;
    }

    public void run() {
        try {
            Thread.sleep(4000L); //Wait four seconds before starting
        } catch (InterruptedException exception) {
            System.out.println("Timer thread has been closed.");
        }
        while (true && !this.isInterrupted()) {
            HttpURLConnection connection = null;
            boolean errored = true;
            try {
                URL url = new URL("http://localhost:" + port + "/timer-run");
                //Create Connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //Write Body
                OutputStream stream = connection.getOutputStream();
                stream.write(((String) "{}").getBytes());
                stream.flush();
                stream.close();
                //Get Response
                String response = String.valueOf(new InputStreamReader(connection.getInputStream()));
                connection.disconnect();
                errored = false;
            } catch (Exception exception) {
                if (!errored) {
                    System.out.println("Timer Thread failed to ping the Jetty application.");
                }
                errored = true;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException exception) {
                    System.out.println("Timer thread has been closed.");
                    break;
                }
            }
        }
    }
}
