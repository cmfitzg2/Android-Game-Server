package client;

import multithreaded.GeneralConstants;
import multithreaded.Handler;
import server.GameServer;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static multithreaded.GeneralConstants.drawYOffset;
import static multithreaded.GeneralConstants.drawXOffset;
import static multithreaded.GeneralConstants.nameSectionHeight;

public class LobbyState extends State {

    private GameHandler handler;
    private boolean firstTick;
    private MouseManager mouseManager;
    private boolean judgeChosen;
    private int scale = GeneralConstants.scale;

    public LobbyState(GameHandler handler) {
        super(handler);
        this.handler = handler;
        firstTick = true;
        judgeChosen = false;
        mouseManager = handler.getMouseManager();
    }

    @Override
    public void tick() {
        if (firstTick) {
            System.out.println("hello from lobby");
            firstTick = false;
        }
        int index = 0;
        int horizontalIndex = 0;
        int verticalIndex;
        for (String id : GameServer.playerIds) {
            Handler handler = GameServer.players.get(id);
            if (null != handler) {
                verticalIndex = index % 4;
                if (index > 3 && index % 4 == 0) {
                    horizontalIndex++;
                }
                int mouseX = mouseManager.getMouseX();
                int mouseY = mouseManager.getMouseY();
                if (this.handler.getMouseManager().isLeftPressed()
                        && playerClicked(verticalIndex, horizontalIndex, mouseX, mouseY)) {
                    handler.setJudge(true);
                    judgeChosen = true;
                    GameServer.judgeHandler = handler;
                }
            }
            index++;
        }
        if (GameServer.drawingSubject != null) {
            State.setState(new PaintGameState(handler));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        if (GameServer.playerCount < GameServer.playerLimit) {
            GeneralUtils.drawCenteredString(g, "Waiting for players",
                    new Rectangle(handler.width, handler.height / 8), Assets.titleFont);
            GeneralUtils.drawCenteredString(g, "Current player count: " + GameServer.playerCount + " / "
                    + GameServer.playerLimit, new Rectangle(handler.width, handler.height / 8 + 140), Assets.titleFont);
            try {
                g.setFont(Assets.smallFont);
                g.setColor(Color.YELLOW);
                g.drawString("IP Address is: " + InetAddress.getLocalHost().toString().split("/")[1], 20, 32);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else if (!judgeChosen) {
            GeneralUtils.drawCenteredString(g, "Choose a judge",
                    new Rectangle(handler.width, handler.height / 8), Assets.titleFont);
        } else {
            GeneralUtils.drawCenteredString(g, "Judge is selecting subject",
                    new Rectangle(handler.width, handler.height / 8), Assets.titleFont);
        }
        int index = 0;
        int horizontalIndex = 0;
        int verticalIndex;
        for (String id : GameServer.playerIds) {
            Handler handler = GameServer.players.get(id);
            if (null != handler && null != handler.getPlayerName()) {
                verticalIndex = index % 4;      //0, 1, 2, 3, 0, 1, 2, 3...
                if (index > 3 && index % 4 == 0) {
                    horizontalIndex++;
                }
                int xStart = getXPosition(verticalIndex);
                int yStart = getYPosition(horizontalIndex);
                g.drawImage(Assets.lobbyFrame, getXPosition(verticalIndex), getYPosition(horizontalIndex),
                        Assets.lobbyFrame.getWidth() * scale, Assets.lobbyFrame.getHeight() * scale, null);
                g.drawImage(handler.getAvatar(), xStart + drawXOffset * scale, yStart + drawYOffset * scale, 64 * scale,
                        64 * scale, null);
                g.setColor(Color.BLACK);
                GeneralUtils.drawCenteredString(g, handler.getPlayerName(),
                        new Rectangle(xStart,
                                yStart + Assets.lobbyFrame.getHeight() * scale  - nameSectionHeight * scale,
                                Assets.lobbyFrame.getWidth() * scale, nameSectionHeight * scale),
                        Assets.nameFont);
            }
            index++;
        }
    }

    @Override
    public void receiveData(Object o, String id) {

    }

    private boolean playerClicked(int xIndex, int yIndex, int mouseX, int mouseY) {
        Rectangle rectangle = new Rectangle(getXPosition(xIndex), getYPosition(yIndex),
                Assets.lobbyFrame.getWidth() * scale, Assets.lobbyFrame.getHeight() * scale);
        return rectangle.contains(mouseX, mouseY);
    }

    private int getXPosition(int verticalIndex) {
        //base offset + offset from the frame next to it
        return 32 + verticalIndex * 32 + verticalIndex * Assets.lobbyFrame.getWidth() * scale;
    }

    private int getYPosition(int horizontalIndex) {
        //base offset + offset from the title font above it + offset from the row above (if it exists)
        return 64 + ((this.handler.height / 8 + 140) / 2) + horizontalIndex * 32
                + horizontalIndex * Assets.lobbyFrame.getHeight() * scale;
    }

    private boolean between(int min, int max, int num) {
        return num > min && num < max;
    }
}
