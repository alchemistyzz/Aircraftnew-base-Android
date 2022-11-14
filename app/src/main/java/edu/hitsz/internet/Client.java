package edu.hitsz.internet;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    Socket socket;
    PrintWriter printWriter;
    BufferedReader inputReader;

    private Client() {
    }

    public static Client getInstance() {
        return ClientHolder.instance;
    }

    static class ClientHolder {
        static Client instance = new Client();
    }

    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new Socket("106.52.3.181", 2233);
                    }
                    printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream(), "UTF-8")), true);
                    inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String str) {
        printWriter.println(str);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        try {
            Thread.sleep(200);
            String receiveMsg;
            receiveMsg = inputReader.readLine();
            return receiveMsg;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public BufferedReader getInputReader() {
        return inputReader;
    }
}

