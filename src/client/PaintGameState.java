package client;

import beans.Drawing;
import multithreaded.GeneralConstants;
import server.GameServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintGameState extends State {

    private Drawing drawing;
    private GameHandler handler;
    private int width = GeneralConstants.drawingWidth, height = GeneralConstants.drawingHeight;
    private int drawXOffset = 42, drawYOffset = 42;
    private int winner;
    private boolean firstTick;
    private boolean drawingFinishedFirst;

    public static Map<String, BufferedImage> playerDrawings;
    public static List<String> playerIDs;

    public PaintGameState(GameHandler handler) {
        super(handler);
        this.handler = handler;
        firstTick = true;
        playerDrawings = new HashMap<>();
        playerIDs = new ArrayList<>();
        drawingFinishedFirst = true;
    }

    @Override
    public void tick() {
        if (firstTick) {
            System.out.println("hello from game state");
            firstTick = false;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        if (playerDrawings.size() >= GameServer.playerLimit - 1) {
            if (drawingFinishedFirst) {
                GameServer.alertJudge = true;
                GameServer.drawingSubject = null;
                drawingFinishedFirst = false;
                //randomize the order
                for (Map.Entry<String, BufferedImage> entry : playerDrawings.entrySet()) {
                    playerIDs.add(entry.getKey());
                }
                Collections.shuffle(playerIDs);
            }
            int index = 0;
            int horizontalIndex = 0;
            int verticalIndex;
            for (String id : playerIDs) {
                verticalIndex = index % 4;      //0, 1, 2, 3, 0, 1, 2, 3...
                if (index > 3 && index % 4 == 0) {
                    horizontalIndex++;
                }
                int xStart = 32 + verticalIndex * 32 + verticalIndex * Assets.artFrameFull.getWidth();
                int yStart = 32 + horizontalIndex * 32 + horizontalIndex * Assets.artFrameFull.getHeight();
                g.drawImage(Assets.artFrameFull, xStart, yStart, null);
                g.drawImage(playerDrawings.get(id), xStart + drawXOffset, yStart + drawYOffset, width, height, null);
                GeneralUtils.drawCenteredString(g, Integer.toString(index + 1),
                        new Rectangle(xStart, yStart, Assets.artFrameFull.getWidth(), drawYOffset), Assets.numberFont);
                index++;
            }
        } else {
            g.setColor(Color.YELLOW);
            g.setFont(Assets.smallFont);
            g.drawString("Received " + GameServer.drawingCount + "/" + (GameServer.playerLimit - 1), 20, 32);
            g.setColor(Color.WHITE);
            GeneralUtils.drawCenteredString(g, "Drawing...",
                    new Rectangle(handler.width, handler.height / 8), Assets.titleFont);
            if (GameServer.drawingSubject != null && !GameServer.drawingSubject.isEmpty()) {
                GeneralUtils.drawCenteredString(g, "Current subject: " + GameServer.drawingSubject,
                        new Rectangle(handler.width, handler.height / 8 + 140), Assets.titleFont);
            }
        }
    }

    @Override
    public void receiveData(Object data, String id) {
        if (data instanceof Drawing) {
            try {
                drawing = (Drawing) data;
                ByteArrayInputStream bis = new ByteArrayInputStream(drawing.getPixels());
                BufferedImage image = ImageIO.read(bis);
                int xStart = drawing.getxStart();
                int yStart = drawing.getyStart();
                int xFinish = drawing.getxFinish();
                int yFinish = drawing.getyFinish();
                image = image.getSubimage(xStart, yStart, xFinish - xStart, yFinish - yStart);
                playerDrawings.put(id, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            winner = (int) data;
            handler.setWinnerNumber(winner);
            State.setState(new PostGameState(handler));
        }
    }
}
