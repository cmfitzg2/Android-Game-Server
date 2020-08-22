package multithreaded;

import server.GameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Lobby {

    private Handler handler;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public static boolean running;

    public Lobby(Handler handler) {
        this.handler = handler;
    }

    public void joinLobby() {
        running = true;
        objectOutputStream = handler.getObjectOutputStream();
        objectInputStream = handler.getObjectInputStream();
        while (running) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(0);
                if (GameServer.lobbyFull) {
                    if (GameServer.chosenGame != null && !GameServer.chosenGame.isEmpty() && GameServer.judgeHandler != null) {
                        notifyGame();
                        GameServer.chosenGame = "";
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String drawingSubject;
        while (GameServer.drawingSubject == null) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (handler.isJudge()) {
                try {
                    if (objectInputStream.available() > 0) {
                        drawingSubject = objectInputStream.readUTF();
                        if (!drawingSubject.isEmpty()) {
                            GameServer.drawingSubject = drawingSubject;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!handler.isJudge()) {
                objectOutputStream.writeUTF(GeneralConstants.startPaintGame);
                objectOutputStream.flush();
            }
            PaintGame paintGame = new PaintGame(handler);
            paintGame.joinPaintGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyGame() {
        try {
            if (GameServer.chosenGame.equals(GeneralConstants.paintGame)) {
                objectOutputStream.writeInt(GameServer.playerLimit);
                objectOutputStream.writeUTF(GeneralConstants.paintGame);
                if (handler.isJudge()) {
                    objectOutputStream.writeUTF("true");
                } else {
                    objectOutputStream.writeUTF("false");
                }
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = false;
    }
}
