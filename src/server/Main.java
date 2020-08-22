package server;

import client.GameClient;
import multithreaded.GeneralConstants;

import java.awt.*;

public class Main {

    private static final int portNumber = 1111;

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GameClient gameClient = new GameClient("famgame", screenSize.width, screenSize.height);
        gameClient.begin();
        GameServer gameServer = new GameServer(portNumber, GeneralConstants.playerLimit);
        gameServer.start();
    }
}
