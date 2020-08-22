package beans;

public class Drawing {
    private int xStart, yStart, xFinish, yFinish;
    private byte[] pixels;

    public Drawing(int xStart, int yStart, int xFinish, int yFinish, byte[] pixels) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xFinish = xFinish;
        this.yFinish = yFinish;
        this.pixels = pixels;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxFinish() {
        return xFinish;
    }

    public int getyFinish() {
        return yFinish;
    }

    public byte[] getPixels() {
        return pixels;
    }
}
