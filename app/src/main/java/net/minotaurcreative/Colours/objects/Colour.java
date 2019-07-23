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

    /**
     * Constructor that creates a colour from the mix of 2 colours
     * @param colour1 first colour
     * @param colour2 second colour
     */
    public Colour(Colour colour1, Colour colour2) {
        this.red = (colour1.getRed() + colour2.getRed()) / 2;
        this.green = (colour1.getGreen() + colour2.getGreen()) / 2;
        this.blue = (colour1.getBlue() + colour2.getBlue()) / 2;
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
