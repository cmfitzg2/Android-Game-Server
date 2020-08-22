package client;

import multithreaded.GeneralConstants;
import multithreaded.Handler;
import server.GameServer;

import java.awt.*;
import java.awt.image.BufferedImage;

import static multithreaded.GeneralConstants.drawXOffset;
import static multithreaded.GeneralConstants.drawYOffset;
import static multithreaded.GeneralConstants.nameSectionHeight;

public class PostGameState extends State {

    private GameHandler handler;
    private boolean firstTick;
    private MouseManager mouseManager;
    private int scale = GeneralConstants.scale;

    public PostGameState(GameHandler handler) {
        super(handler);
        this.handler = handler;
        firstTick = true;
        mouseManager = handler.getMouseManager();
    }

    @Override
    public void tick() {
        if (firstTick) {
            System.out.println("hello from post-game state");
            firstTick = false;
        }
        if (GeneralUtils.areaClicked(mouseManager,
                new Rectangle(handler.width / 2 - 128, handler.height - 160, 256, 128))) {
            GameServer.drawingCount = 0;
            State.setState(new LobbyState(handler));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        GeneralUtils.drawCenteredString(g, "our big winner is...",
                new Rectangle(handler.width, handler.height / 4), Assets.bigFont);
        Handler player = GameServer.players.get(PaintGameState.playerIDs.get(handler.getWinnerNumber() - 1));
        int xStartPlayer = handler.width / 2 - Assets.lobbyFrame.getWidth() * scale - 16;
        int yStartPlayer = handler.height / 2 - Assets.lobbyFrame.getHeight() * scale / 2;
        int xStartDrawing = handler.width / 2 + 16;
        int yStartDrawing = handler.height / 2 - Assets.lobbyFrame.getHeight() * scale / 2;
        g.drawImage(Assets.lobbyFrame, xStartPlayer, yStartPlayer,
                Assets.lobbyFrame.getWidth() * scale, Assets.lobbyFrame.getHeight() * scale, null);
        g.drawImage(player.getAvatar(), xStartPlayer + drawXOffset * scale, yStartPlayer + drawYOffset * scale,
                64 * scale, 64 * scale, null);
        g.setColor(Color.BLACK);
        GeneralUtils.drawCenteredString(g, player.getPlayerName(),
                new Rectangle(xStartPlayer,
                        yStartPlayer + Assets.lobbyFrame.getHeight() * scale  - nameSectionHeight * scale,
                        Assets.lobbyFrame.getWidth() * scale, nameSectionHeight * scale), Assets.nameFont);
        BufferedImage drawing = PaintGameState.playerDrawings.get(player.getId());
        g.drawImage(drawing, xStartDrawing, yStartDrawing, GeneralConstants.drawingWidth,
                GeneralConstants.drawingHeight, null);
        g.drawImage(Assets.nextButton, handler.width / 2 - 128, handler.height - 160, 256, 128, null);
    }

    @Override
    public void receiveData(Object o, String id) {

    }
}
