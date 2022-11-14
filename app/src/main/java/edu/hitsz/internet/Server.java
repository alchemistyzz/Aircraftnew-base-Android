package edu.hitsz.internet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class Server {

    private ServerSocket serverSocket;
    private Vector<SocketThread> socketThreads;
    private Vector<SocketThread> socketCombat;
    private CheckCombat checkCombat;

    public static void main(String[] args) {
        Server.getInstance().accept();
    }

    public static Server getInstance() {
        return ServerHolder.instance;
    }

    static class ServerHolder {
        static Server instance = new Server();
    }

    private Server() {
        try {
            serverSocket = new ServerSocket(2233);
            socketThreads = new Vector<>();
            socketCombat = new Vector<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accept() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(checkCombat = new CheckCombat()).start();
                try {
                    while (true) {
                        Socket client = serverSocket.accept();
                        SocketThread socketThread = new SocketThread(client);
                        new Thread(socketThread).start();
                        socketThreads.add(socketThread);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class CheckCombat implements Runnable {
        @Override
        public synchronized void run() {
            while (true) {
                System.out.println("checking");
                if (socketCombat.size() >= 2) {
                    System.out.println("checkCombat2");
                    SocketThread socketThread1 = socketCombat.get(0);
                    socketCombat.remove(0);
                    SocketThread socketThread2 = socketCombat.get(0);
                    socketCombat.remove(0);

                    socketThread1.sendMessage("startCombat");
                    socketThread2.sendMessage("startCombat");
                    new Thread(new CombatThread(socketThread1, socketThread2)).start();
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("checkCombat22");
                } else if (socketCombat.size() == 1) {
                    System.out.println("checkCombat1");
                    SocketThread socketThread = socketCombat.get(0);
                    try {
                        if (socketThread.bufferedReader.ready()) {
                            System.out.println("checkCombat11");
                            String msg = socketThread.getMessage();
                            if (msg.equals("quitCombat")) {
                                System.out.println("one quit");
                                socketCombat.remove(0);
                                synchronized (socketThread) {
                                    socketThread.notify();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class SocketThread implements Runnable {
        private Socket socket;
        private PrintWriter printWriter;
        private BufferedReader bufferedReader;

        SocketThread(Socket socket) {
            this.socket = socket;
            try {
                System.out.println("socket connect succeed");
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        this.socket.getOutputStream(), "UTF-8")), true);
                bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                receiveMsg = bufferedReader.readLine();
                return receiveMsg;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public synchronized void run() {
            try {
                String receiveMsg = "";
                while ((receiveMsg = bufferedReader.readLine()) != null) {
                    if (receiveMsg.equals("waitForCombat")) {
                        System.out.println("one wait");
                        socketCombat.add(this);
                        this.wait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class CombatThread implements Runnable {

        private SocketThread socketThread1;
        private SocketThread socketThread2;

        CombatThread(SocketThread socketThread1, SocketThread socketThread2) {
            this.socketThread1 = socketThread1;
            this.socketThread2 = socketThread2;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("bbb");
                String string1 = socketThread1.getMessage();
                String string2 = socketThread2.getMessage();
                System.out.println("ccc");
                System.out.println("score1:" + string1);
                System.out.println("score2:" + string2);
                if (!Objects.equals(string1, "GameOver")) {
                    System.out.println("sendTo11");
                    socketThread2.sendMessage(string1);
                    System.out.println("sendTo12");
                }
                if (!Objects.equals(string2, "GameOver")) {
                    System.out.println("sendTo21");
                    socketThread1.sendMessage(string2);
                    System.out.println("sendTo22");
                }
                if (string1.equals(string2) && string1.equals("GameOver")) {
                    System.out.println("CombatOver");
                    socketThread1.sendMessage("combatOver");
                    socketThread2.sendMessage("combatOver");
                    synchronized (socketThread1) {
                        socketThread1.notify();
                    }
                    synchronized (socketThread2) {
                        socketThread2.notify();
                    }
                    synchronized (checkCombat) {
                        checkCombat.notify();
                    }
                    break;
                }
                if (string1.equals("GameOver")) {
                    socketThread2.sendMessage("opponentOver");
                }
                if (string2.equals("GameOver")) {
                    socketThread1.sendMessage("opponentOver");
                }
                System.out.println("aaa");
            }
        }
    }

}


