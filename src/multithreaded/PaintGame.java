package multithreaded;

import beans.Drawing;
import server.GameServer;

import java.io.IOException;

public class PaintGame {

    private Handler handler;
    public static boolean running;

    public PaintGame(Handler handler) {
        this.handler = handler;
    }

    public void joinPaintGame() {
        running = true;
        while (running) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!handler.isJudge()) {
                try {
                    if (handler.getObjectInputStream().available() > 0) {
                        int xStart = handler.getObjectInputStream().readInt();
                        int yStart = handler.getObjectInputStream().readInt();
                        int xFinish = handler.getObjectInputStream().readInt();
                        int yFinish = handler.getObjectInputStream().readInt();
                        int size = handler.getObjectInputStream().readInt();
                        byte[] pixels = new byte[size];
                        handler.getObjectInputStream().readFully(pixels);
                        Drawing drawing = new Drawing(xStart, yStart, xFinish, yFinish, pixels);
                        GameServer.finishDrawing(drawing, handler.getId());
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (GameServer.alertJudge) {
                    try {
                        handler.getObjectOutputStream().writeUTF(GeneralConstants.drawingFinished);
                        handler.getObjectOutputStream().flush();
                        GameServer.alertJudge = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    //receive rating
                    if (handler.getObjectInputStream().available() > 0) {
                        System.out.println("receiving winner");
                        int winner = handler.getObjectInputStream().readInt();
                        System.out.println("winner number is: " + winner);
                        GameServer.receiveWinner(winner, handler.getId());
                        handler.setJudge(false);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        GameServer.judgeHandler = null;
        GameServer.chosenGame = GeneralConstants.paintGame;
        Lobby lobby = new Lobby(handler);
        lobby.joinLobby();
    }
}
