package edu.hitsz.internet;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private Socket socket;
    private String ip;

    public Server(String ip) {
        this.ip = ip;
    }

    public void sendMessage(String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                try {
                    socket = new Socket();

//ip为电脑所在的局域网网址

                    socket.connect(new InetSocketAddress(ip, 2233), 5000);

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(str.getBytes());
                    outputStream.flush();

                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = null;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = bfr.readLine()) != null) {
                        buffer.append(line);
                    }
                    result = buffer.toString();
                    bfr.close();
                    outputStream.close();
                    socket.close();
                } catch (SocketException e) {
//连接超时，在UI界面显示消息
                    Log.i("socket", e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}