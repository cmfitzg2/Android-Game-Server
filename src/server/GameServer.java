package server;

import beans.Drawing;
import client.Assets;
import client.GameClient;
import multithreaded.ConnectionHandler;
import multithreaded.Handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class GameServer {

    private int portNumber;
    public static int drawingCount;
    public static int playerLimit;
    public static int playerCount;
    public static boolean lobbyFull = false;
    public static String chosenGame = "Paint Game";
    public static Map<String, Handler> players = new HashMap<>();
    public static List<String> playerIds = new ArrayList<>();
    public static Handler judgeHandler;
    public static String drawingSubject;
    public static boolean alertJudge = false;

    public GameServer(int portNumber, int playerLimit) {
        this.portNumber = portNumber;
        this.playerLimit = playerLimit;
    }

    public void start() {
        playerCount = 0;
        ServerSocket serverSocket = null;
        Handler handler;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is listening for connections to " + InetAddress.getLocalHost()
                    + " on port " + portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (playerCount < playerLimit) {
            try {
                if (serverSocket != null) {
                    Socket clientSocket = serverSocket.accept();
                    handler = new Handler();
                    String id = UUID.randomUUID().toString();
                    playerIds.add(id);
                    handler.setId(id);
                    handler.setPlayerSocket(clientSocket);
                    handler.setAvatar(Assets.getRandomAvatar());
                    handler.setObjectInputStream(new ObjectInputStream(clientSocket.getInputStream()));
                    handler.setObjectOutputStream(new ObjectOutputStream(clientSocket.getOutputStream()));
                    ConnectionHandler connectionHandler = new ConnectionHandler(handler);
                    Thread playerThread = new Thread(connectionHandler);
                    playerThread.start();
                    players.put(id, handler);
                    playerCount++;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        lobbyFull = true;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveDrawings();
    }

    public static void receiveDrawings() {
        while (playerCount - 1 > drawingCount) {

        }
    }

    public synchronized static void finishDrawing(Drawing drawing, String id) {
        GameClient.receiveData(drawing, id);
        drawingCount++;
    }

    public static void receiveWinner(int rating, String id) {
        GameClient.receiveData(rating, id);
    }
}
