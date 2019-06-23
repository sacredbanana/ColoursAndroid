package net.minotaurcreative.Colours.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.*;

/**
 * Main drawing class.
 * @author Cameron Armstrong
 */
public class ColourScreen {
    final int SCREEN_WIDTH = 1280;
    final int SCREEN_HEIGHT = 640;

    /**
     * Total number of colours = 32^3
     */
    private final int TOTAL_COLOURS = 32768;

    /**
     * Number of algorithms used
     */
    private final int NUM_ALGORITHMS = 9;

    /**
     * Current algorithm being used
     */
    private int currentAlgorithm = 0;

    /**
     * Position in colour cycle
     */
    private int cycle = 0;

    /**
     * Speed of colour cycling
     */
    private int cycleSpeed = 0;

    /**
     * Menu visibility
     */
    private boolean menuVisible = true;

    /**
     * Name of current algorithm
     */
    private String currentAlgorithmName = "Colour Cube Slice";

    /**
     * ArrayList containing all colours
     */
    private ArrayList<Colour> colours;

    private int[] buffer;

    /**
     * Constructor
     */
    public ColourScreen() {
//        colours = new ArrayList<>(TOTAL_COLOURS);

        colourCubeSlice(); // Start first algorithm

//        Timer timer = new Timer(1, e -> { // Add timer allowing colour cycling animation
//            cycle += cycleSpeed;
//            if (cycle > 255)
//                cycle = 0;
//            repaint();
//        });
//        timer.start();
    }

    /**
     * Main drawing method
     */
    public Bitmap generateBitmap() {
        return Bitmap.createBitmap(buffer, TOTAL_COLOURS/256*2, TOTAL_COLOURS/256, Bitmap.Config.ARGB_8888);
//
//        // Draw all colours contained in the ArrayList "colours"
//        for (Colour colour : colours) {
//            // Get position
////            int coordX = colour.xPos;
////            int coordY = colour.yPos;
//
//            // Adjust for colour cycling
//            int red = colour.getRed() + cycle;
//            if (red > 255)
//                red -= 255;
//            int green = colour.getGreen() + cycle;
//            if (green > 255)
//                green -= 255;
//            int blue = colour.getBlue() + cycle;
//            if (blue > 255)
//                blue -= 255;
////            Color adjustedColour = new Color(red, green, blue);
////            g.setColor(adjustedColour);
//
//            // Define block size
//            final int BLOCK_WIDTH = 5;
//            final int BLOCK_HEIGHT = 5;
//
//            Bitmap.Config config = Bitmap.Config.ARGB_8888;
//            return Bitmap.createBitmap(buffer, TOTAL_COLOURS/256*2, TOTAL_COLOURS/256, Bitmap.Config.ARGB_8888);
//
//            // Draw block
////            g.fillRect(colour.xPos * BLOCK_WIDTH, colour.yPos * BLOCK_HEIGHT,BLOCK_WIDTH, BLOCK_HEIGHT);
//        }
//
//        // Draw menu
////        if (menuVisible) {
////            g.setColor(Color.LIGHT_GRAY);
////            g.fillRect(0,0,getWidth(),20);
////            g.setColor(Color.BLACK);
////            g.setFont(new Font("Serif", Font.BOLD,12));
////            g.drawString("↑: Cycle speed up", 10,14);
////            g.drawString("↓: Cycle speed down", 150, 14);
////            g.drawString("SPACE: Switch algorithm", 330, 14);
////            g.drawString("M: Show/hide menu", 530, 14);
////            g.drawString("Current Algorithm: " + currentAlgorithmName, 730,14);
////        }
    }

    /**
     * Calculates average of 2 colours
     * @param colour1 first colour
     * @param colour2 second colour
     * @return averaged colour
     */
    private Colour averageColour(Colour colour1, Colour colour2) {
        int red = (colour1.getRed() + colour2.getRed()) / 2;
        int green = (colour1.getGreen() + colour2.getGreen()) / 2;
        int blue = (colour1.getBlue() + colour2.getBlue()) / 2;
        return new Colour(red, green, blue);
    }

    /**
     * Finds nearest colour to origin colour
     * @param originColour colour you want the nearest colour to
     * @return nearest colour
     */
    private Colour nearestColour(Colour originColour) {
        int minDistance = Integer.MAX_VALUE;
        Colour closestColour = colours.get(0);

        for (Colour destinationColour : colours) {
            // Calculates distance in colour cube by pythagoras minus the square root step to improve performance
            int redComponent = originColour.getRed() - destinationColour.getRed();
            int greenComponent = originColour.getGreen() - destinationColour.getGreen();
            int blueComponent = originColour.getBlue() - destinationColour.getBlue();
            int distance = redComponent * redComponent + greenComponent * greenComponent + blueComponent * blueComponent;
            if (distance < minDistance) {
                closestColour = destinationColour;
                minDistance = distance;
            }
        }

        return closestColour;
    }

    /**
     * Increases colour cycle speed
     */
    public void increaseCycleSpeed() {
        cycleSpeed++;
    }

    /**
     * Decreases colour cycle speed
     */
    public void decreaseCycleSpeed() {
        if (--cycleSpeed <=  0) {
            cycleSpeed = 0;
            cycle = 0;
        }
    }

    /**
     * Shows/hides menu
     */
    public void toggleMenu() {
        menuVisible = !menuVisible;
    }

    /**
     * Switches algorithm
     */
    public void switchAlgorithm() {
        colours.clear();
        cycleSpeed = 0;
        cycle = 0;
        if (++currentAlgorithm >= NUM_ALGORITHMS)
            currentAlgorithm = 0;
        switch (currentAlgorithm) {
            case 0:
                colourCubeSlice();
                break;
            case 1:
                smooth();
                break;
            case 2:
                randomSpread();
                break;
            case 3:
//                clumpColours();
                break;
            case 4:
//                nearestToPrevious();
                break;
            case 5:
//                nearestToAbove();
                break;
            case 6:
//                nearestToAboveAndPrevious();
                break;
            case 7:
//                nearestToAllThreeAbove();
                break;
            case 8:
//                nearestToAllThreeAboveAndPrevious();
                break;
        }
    }

    /**
     * ALGORITHM: Colour Cube Slice
     * Colours added by slicing colour cube
     */
    private void colourCubeSlice() {
        currentAlgorithmName = "Colour Cube Slice";

        int xCoord = 0;
        int yCoord = 0;

        for (int red = 0; red <= 0xff; red += 8) {
            for (int green = 0; green < 256; green += 8) {
                for (int blue = 0; blue < 256; blue += 8) {
                    buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+1] = red;
                    buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+2] = green;
                    buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+3] = blue;
//                    Colour colour = new Colour(red, green, blue, xCoord, yCoord);
                    if ((++xCoord) == 256) {
                        xCoord = 0;
                        yCoord++;
                    }
//                    colours.add(colour);
                }
            }
        }
    }

    /**
     * ALGORITHM Colour Cube Slice with Smoothing
     * Same as Colour Cube Slice but the colours alternate directions for a smoother appearance
     */
    private void smooth() {
        currentAlgorithmName = "Colour Cube Slice with Smoothing";

        int xCoord = 0;
        int yCoord = 0;
        boolean blueGradientUp = true;
        boolean greenGradientUp = true;

        for (int red = 0; red < 256; red += 8) {
            if (greenGradientUp) {
                for (int green = 0; green < 256; green += 8) {
                    if (blueGradientUp) {
                        for (int blue = 0; blue < 256; blue += 8) {
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+1] = red;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+2] = green;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+3] = blue;
//                            Colour colour = new Colour(red, green, blue, xCoord, yCoord);
                            if ((++xCoord) >= 256) {
                                xCoord = 0;
                                yCoord++;
                            }
//                            colours.add(colour);
                        }
                    } else {
                        for (int blue = 248; blue >= 0; blue -= 8) {
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+1] = red;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+2] = green;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+3] = blue;
//                            Colour colour = new Colour(red, green, blue, xCoord, yCoord);
                            if ((++xCoord) >= 256) {
                                xCoord = 0;
                                yCoord++;
                            }
//                            colours.add(colour);
                        }
                    }
                    blueGradientUp = !blueGradientUp;
                }
            } else {
                for (int green = 248; green >= 0; green -= 8) {
                    if (blueGradientUp) {
                        for (int blue = 0; blue < 256; blue += 8) {
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+1] = red;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+2] = green;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+3] = blue;
//                            Colour colour = new Colour(red, green, blue, xCoord, yCoord);
                            if ((++xCoord) >= 256) {
                                xCoord = 0;
                                yCoord++;
                            }
//                            colours.add(colour);
                        }
                    } else {
                        for (int blue = 248; blue >= 0; blue -= 8) {
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+1] = red;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+2] = green;
                            buffer[yCoord*TOTAL_COLOURS/256*2*4+xCoord+3] = blue;
//                            Colour colour = new Colour(red, green, blue, xCoord, yCoord);
                            if ((++xCoord) >= TOTAL_COLOURS/256*2*3) {
                                xCoord = 0;
                                yCoord++;
                            }
//                            colours.add(colour);
                        }
                    }
                    blueGradientUp = !blueGradientUp;
                }
            }
            greenGradientUp = !greenGradientUp;
        }
    }

    /**
     * ALGORITHM: Random Spread
     * Distributes all colours randomly
     */
    private void randomSpread() {
        colourCubeSlice();

        currentAlgorithmName = "Random Spread";
        Random randomGenerator = new Random();

        for (int y = 0; y < TOTAL_COLOURS/256; y++) {
            for (int x = 0; x < TOTAL_COLOURS/256*2*4; x += 4) {
                int randomXIndex = randomGenerator.nextInt(TOTAL_COLOURS/256*2*4);
                int randomYIndex = randomGenerator.nextInt(TOTAL_COLOURS/256);
                Colour tempColour = new Colour(buffer[y*TOTAL_COLOURS/256*2*4+x+1], buffer[y*TOTAL_COLOURS/256*2*4+x+2], buffer[y*TOTAL_COLOURS/256*2*4+x+3]);
                buffer[y*TOTAL_COLOURS/256*2*4+x+1] = buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+1];
                buffer[y*TOTAL_COLOURS/256*2*4+x+2] = buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+2];
                buffer[y*TOTAL_COLOURS/256*2*4+x+3] = buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+3];
                buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+1] = tempColour.getRed();
                buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+2] = tempColour.getGreen();
                buffer[randomYIndex*TOTAL_COLOURS/256*2*4+randomXIndex+3] = tempColour.getBlue();
            }
        }
    }

    /**
     * ALGORITHM: Colour Cube Slice with Red Accumulation
     * Same as Colour Cube Slice but gets the red most colours first
     */
//    private void clumpColours() {
//        colourCubeSlice();
//        currentAlgorithmName = "Colour Cube Slice with Red Accumulation";
//
//        // Sort by redness
//        Collections.sort(colours, (colour2, colour1) -> {
//            if (colour1.getRed() < colour2.getRed())
//                return -1;
//            if (colour1.getRed() == colour2.getRed())
//                return 0;
//            return 1;
//        });
//
//        // Update positions
//        Iterator<Colour> iterator = colours.iterator();
//        for (int y = 0; y < 128; y++) {
//            for (int x = 0; x < 256; x++) {
//                Colour colour = iterator.next();
//                colour.xPos = x;
//                colour.yPos = y;
//            }
//        }
//    }

    /**
     * ALGORITHM: Nearest Colour to Previous Block
     * The next colour picked is the closest colour to the block to the left
     */
//    private void nearestToPrevious() {
//        colourCubeSlice();
//        currentAlgorithmName = "Nearest Colour to Previous Block";
//
//        ArrayList<Colour> buffer = new ArrayList<>(TOTAL_COLOURS); // Buffer to store new colours
//
//        Colour firstColour = colours.remove(0);
//        firstColour.xPos = 0;
//        firstColour.yPos = 0;
//        buffer.add(firstColour);
//
//        while (!colours.isEmpty()) {
//            Colour currentColour = buffer.get(buffer.size()-1);
//            Colour closestColour = nearestColour(currentColour);
//            colours.remove(closestColour);
//            int x = currentColour.xPos;
//            int y = currentColour.yPos;
//            if (++x >= 256) {
//                x = 0;
//                y++;
//            }
//            closestColour.xPos = x;
//            closestColour.yPos = y;
//            buffer.add(closestColour);
//        }
//        colours = buffer;
//    }

    /**
     * ALGORITHM: Nearest Colour to Block Above
     * The next colour picked is the closest colour to the block above
     */
//    private void nearestToAbove() {
//        colourCubeSlice();
//        currentAlgorithmName = "Nearest Colour to Block Above";
//        ArrayList<Colour> buffer = new ArrayList<>(TOTAL_COLOURS); // Buffer to store new colours
//
//        Colour firstColour = colours.remove(0);
//        firstColour.xPos = 0;
//        firstColour.yPos = 0;
//        buffer.add(firstColour);
//
//        int x = 0;
//        int y = 0;
//
//        while (!colours.isEmpty()) {
//            Colour currentColour;
//            if (y > 0) {
//                currentColour = buffer.get(buffer.size() - 256);
//            } else {
//                currentColour = buffer.get(buffer.size()-1);
//            }
//
//            Colour closestColour = nearestColour(currentColour);
//            colours.remove(closestColour);
//            if (++x >= 256) {
//                x = 0;
//                y++;
//            }
//            closestColour.xPos = x;
//            closestColour.yPos = y;
//            buffer.add(closestColour);
//        }
//        colours = buffer;
//    }

    /**
     * ALGORITHM: Nearest Colour to Both Above and Previous Blocks
     * The next colour picked is the closest colour to the average of the blocks to the left and above
     */
//    private void nearestToAboveAndPrevious() {
//        colourCubeSlice();
//        currentAlgorithmName = "Nearest Colour to Both Above and Previous Blocks";
//        ArrayList<Colour> buffer = new ArrayList<>(TOTAL_COLOURS); // Buffer to store new colours
//
//        Colour firstColour = colours.remove(0);
//        firstColour.xPos = 0;
//        firstColour.yPos = 0;
//        buffer.add(firstColour);
//
//        int x = 0;
//        int y = 0;
//
//        while (!colours.isEmpty()) {
//            Colour currentColour;
//            Colour currentColour2;
//            if (y > 0) {
//                currentColour = buffer.get(buffer.size() - 256);
//                currentColour2 = buffer.get(buffer.size() - 1);
//                currentColour = averageColour(currentColour, currentColour2);
//            } else {
//                currentColour = buffer.get(buffer.size() - 1);
//            }
//
//            Colour closestColour = nearestColour(currentColour);
//            colours.remove(closestColour);
//            if (++x >= 256) {
//                x = 0;
//                y++;
//            }
//            closestColour.xPos = x;
//            closestColour.yPos = y;
//            buffer.add(closestColour);
//        }
//        colours = buffer;
//    }

    /**
     * ALGORITHM: Nearest Colour to All Three Blocks Above
     * The next colour picked is the closest colour to the average of all 3 blocks above
     */
//    private void nearestToAllThreeAbove() {
//        colourCubeSlice();
//        currentAlgorithmName = "Nearest Colour to All Three Blocks Above";
//        ArrayList<Colour> buffer = new ArrayList<>(TOTAL_COLOURS); // Buffer to store new colours
//
//        Colour firstColour = colours.remove(0);
//        firstColour.xPos = 0;
//        firstColour.yPos = 0;
//        buffer.add(firstColour);
//
//        int x = 0;
//        int y = 0;
//
//        while (!colours.isEmpty()) {
//            Colour currentColour;
//            Colour currentColour2;
//            if (y > 0) {
//                currentColour = buffer.get(buffer.size() - 256);
//                currentColour2 = buffer.get(buffer.size() - 255);
//                currentColour = averageColour(currentColour, currentColour2);
//                if (buffer.size() > 256) {
//                    currentColour2 = buffer.get(buffer.size() - 257);
//                    currentColour = averageColour(currentColour, currentColour2);
//                }
//                currentColour2 = buffer.get(buffer.size() - 1);
//                currentColour = averageColour(currentColour, currentColour2);
//            } else {
//                currentColour = buffer.get(buffer.size() - 1);
//            }
//
//            Colour closestColour = nearestColour(currentColour);
//            colours.remove(closestColour);
//            if (++x >= 256) {
//                x = 0;
//                y++;
//            }
//            closestColour.xPos = x;
//            closestColour.yPos = y;
//            buffer.add(closestColour);
//        }
//        colours = buffer;
//    }

    /**
     * ALGORITHM: Nearest Colour to All Three Pixels Above Plus Previous
     * The next colour picked is the closest colour to the average of all 3 blocks above and the one to the left
     */
//    private void nearestToAllThreeAboveAndPrevious() {
//        colourCubeSlice();
//        currentAlgorithmName = "Nearest Colour to All Three Pixels Above Plus Previous";
//        ArrayList<Colour> buffer = new ArrayList<>(TOTAL_COLOURS); // Buffer to store new colours
//
//        Colour firstColour = colours.remove(0);
//        firstColour.xPos = 0;
//        firstColour.yPos = 0;
//        buffer.add(firstColour);
//
//        int x = 0;
//        int y = 0;
//
//        while (!colours.isEmpty()) {
//            Colour currentColour;
//            Colour currentColour2;
//            if (y > 0) {
//                currentColour = buffer.get(buffer.size() - 256);
//                currentColour2 = buffer.get(buffer.size() - 255);
//                currentColour = averageColour(currentColour, currentColour2);
//                if (buffer.size() > 256) {
//                    currentColour2 = buffer.get(buffer.size() - 257);
//                    currentColour = averageColour(currentColour, currentColour2);
//                }
//            } else {
//                currentColour = buffer.get(buffer.size() - 1);
//            }
//
//            Colour closestColour = nearestColour(currentColour);
//            colours.remove(closestColour);
//            if (++x >= 256) {
//                x = 0;
//                y++;
//            }
//            closestColour.xPos = x;
//            closestColour.yPos = y;
//            buffer.add(closestColour);
//        }
//        colours = buffer;
//    }
}