package multithreaded;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConnectionHandler implements Runnable {

    private Handler handler;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ConnectionHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            objectInputStream = handler.getObjectInputStream();
            objectOutputStream = handler.getObjectOutputStream();
            handler.setPlayerName(objectInputStream.readUTF());
            System.out.println(handler.getPlayerName());
            Lobby lobby = new Lobby(handler);
            lobby.joinLobby();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
