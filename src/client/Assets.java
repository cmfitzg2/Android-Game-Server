package client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Assets {
    private static final int width = 32, height = 32;

    public static BufferedImage avatarRed, avatarOrange, avatarYellow, avatarGreen, avatarBlue, avatarPurple,
            avatarPink, avatarGray, avatarMissing, artFrameFull, lobbyFrame;
    public static BufferedImage nextButton;
    public static Font titleFont, bigFont, smallFont, nameFont, numberFont;
    private static Random random;
    private static List<BufferedImage> avatars = new ArrayList<>();

    static void init(){
        titleFont = new Font("Calibri", Font.BOLD, 64);
        bigFont = new Font("Calibri", Font.BOLD, 128);
        numberFont = new Font("Segoe Script", Font.PLAIN, 48);
        smallFont = new Font("Calibri", Font.PLAIN, 32);
        nameFont  = new Font("Courier New", Font.BOLD, 40);
        random = new Random();
        nextButton = ImageLoader.loadImage("/ui/NextButton.png");
        avatarRed = ImageLoader.loadImage("/avatars/red.png");
        avatarOrange = ImageLoader.loadImage("/avatars/orange.png");
        avatarYellow = ImageLoader.loadImage("/avatars/yellow.png");
        avatarGreen = ImageLoader.loadImage("/avatars/green.png");
        avatarBlue = ImageLoader.loadImage("/avatars/blue.png");
        avatarPurple = ImageLoader.loadImage("/avatars/purple.png");
        avatarPink = ImageLoader.loadImage("/avatars/pink.png");
        avatarGray = ImageLoader.loadImage("/avatars/gray.png");
        avatarMissing = ImageLoader.loadImage("/avatars/missing.png");
        artFrameFull = ImageLoader.loadImage("/avatars/art-frame-full.png");
        lobbyFrame = ImageLoader.loadImage("/avatars/lobby-frame.png");

        avatars.add(avatarRed);
        avatars.add(avatarOrange);
        avatars.add(avatarYellow);
        avatars.add(avatarGreen);
        avatars.add(avatarBlue);
        avatars.add(avatarPurple);
        avatars.add(avatarPink);
        avatars.add(avatarGray);
    }

    public static BufferedImage getRandomAvatar() {
        if (!avatars.isEmpty()) {
            int randInt = random.nextInt(avatars.size());
            BufferedImage image = avatars.get(randInt);
            avatars.remove(randInt);
            return image;
        }
        return avatarMissing;
    }
}
