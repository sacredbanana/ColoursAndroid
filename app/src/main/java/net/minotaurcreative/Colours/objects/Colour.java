package net.minotaurcreative.Colours.objects;

/**
 * Colour class
 */
public class Colour {
    /**
     * Red value
     */
    private int red;

    /**
     * Green value
     */
    private int green;

    /**
     * Blue value
     */
    private int blue;

    public Colour(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
