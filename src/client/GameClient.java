package client;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameClient implements Runnable {

    private Display display;
    private int width, height;
    public String title;

    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private LobbyState lobbyState;

    private GameHandler gameHandler;

    private boolean debugMode = false;

    //Input
    private MouseManager mouseManager;

    public GameClient(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        mouseManager = new MouseManager();
    }

    @Override
    public void run() {
        init();
        int fps = 60;
        double timePerTick = (double) 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while(running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;
            if(delta >= 1) {
                tick();
                render();
                delta = 0;
            }

        }

        stop();
    }

    private void init() {
        Assets.init();
        display = new Display(title, width, height);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        gameHandler = new GameHandler();
        gameHandler.setMouseManager(mouseManager);
        gameHandler.setWidth(width);
        gameHandler.setHeight(height);
        lobbyState = new LobbyState(gameHandler);
        if (debugMode) {
            State.setState(new PaintGameState(gameHandler));
        } else {
            State.setState(lobbyState);
        }
    }

    private void tick() {
        if(State.getState() != null) {
            State.getState().tick();
        }
    }

    private void render()
    {
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //clear screen
        g.clearRect(0, 0, width, height);

        //draw
        if(State.getState() != null) {
            State.getState().render(g);
        }
        else {
            System.out.println("no state");
        }

        //done drawing
        bs.show();
        g.dispose();
    }

    public synchronized void stop() {
        if(!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void begin() {
        if(running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public static void receiveData(Object o, String id) {
        State.getState().receiveData(o, id);
    }
}
