package net.minotaurcreative.Colours.objects;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;
import net.minotaurcreative.Colours.R;
import net.minotaurcreative.Colours.enums.AlgorithmType;
import net.minotaurcreative.Colours.views.CanvasView;

import java.util.*;

/**
 * Main drawing class.
 * @author Cameron Armstrong
 */
public class ColourScreen {
    public CanvasView canvasView;

    private ProcessImageTask processImageTask;

    private Bitmap generatedBitmap;

    private Bitmap resizedBitmap;

    public class ProcessImageTask extends AsyncTask<AlgorithmType, Integer, Void> {

        @Override
        protected Void doInBackground(AlgorithmType... algorithmTypes) {
            switch (algorithmTypes[0]) {
                case COLOUR_CUBE_SLICE:
                    colourCubeSlice();
                    break;
                case COLOUR_CUBE_SLICE_WITH_SMOOTHING:
                    smooth();
                    break;
                case RANDOM_SPREAD:
                    randomSpread();
                    break;
                case NEAREST_TO_PREVIOUS_COLOUR:
                    nearestToPrevious();
                    break;
                case NEAREST_TO_BLOCK_ABOVE:
                    nearestToAbove();
                    break;
                case NEAREST_TO_BOTH_ABOVE_AND_PREVIOUS_BLOCKS:
                    nearestToAboveAndPrevious();
                    break;
                case NEAREST_TO_ALL_THREE_BLOCKS_ABOVE:
                    nearestToAllThreeAbove();
                    break;
                case NEAREST_TO_ALL_THREE_PIXELS_ABOVE_PLUS_PREVIOUS:
                    nearestToAllThreeAboveAndPrevious();
                    break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);

            Log.i("progress: ", String.format("%d",integers[0]));
            generateBitmap();
            canvasView.postInvalidate();
        }

        /**
         * ALGORITHM: Colour Cube Slice
         * Colours added by slicing colour cube
         */
        public void colourCubeSlice() {
            int xCoord = 0;
            int yCoord = 0;

            for (int red = 0; red <= 0xff; red += 8) {
                for (int green = 0; green < 256; green += 8) {
                    for (int blue = 0; blue < 256; blue += 8) {
                        buffer[yCoord*TOTAL_COLOURS/256*2+xCoord] = packColourData(red, green, blue);
                        if (++xCoord >= TOTAL_COLOURS/256*2) {
                            xCoord = 0;
                            yCoord++;
                        }
                    }
                }
            }
        }

        /**
         * ALGORITHM Colour Cube Slice with Smoothing
         * Same as Colour Cube Slice but the colours alternate directions for a smoother appearance
         */
        private void smooth() {
            int xCoord = 0;
            int yCoord = 0;
            boolean blueGradientUp = true;
            boolean greenGradientUp = true;

            for (int red = 0; red < 256; red += 8) {
                if (greenGradientUp) {
                    for (int green = 0; green < 256; green += 8) {
                        if (blueGradientUp) {
                            for (int blue = 0; blue < 256; blue += 8) {
                                buffer[yCoord*TOTAL_COLOURS/256*2+xCoord] = packColourData(red, green, blue);
                                if ((++xCoord) >= 256) {
                                    xCoord = 0;
                                    yCoord++;
                                }
                            }
                        } else {
                            for (int blue = 248; blue >= 0; blue -= 8) {
                                buffer[yCoord*TOTAL_COLOURS/256*2+xCoord] = packColourData(red, green, blue);
                                if ((++xCoord) >= 256) {
                                    xCoord = 0;
                                    yCoord++;
                                }
                            }
                        }
                        blueGradientUp = !blueGradientUp;
                    }
                } else {
                    for (int green = 248; green >= 0; green -= 8) {
                        if (blueGradientUp) {
                            for (int blue = 0; blue < 256; blue += 8) {
                                buffer[yCoord*TOTAL_COLOURS/256*2+xCoord] = packColourData(red, green, blue);
                                if ((++xCoord) >= 256) {
                                    xCoord = 0;
                                    yCoord++;
                                }
                            }
                        } else {
                            for (int blue = 248; blue >= 0; blue -= 8) {
                                buffer[yCoord*TOTAL_COLOURS/256*2+xCoord] = packColourData(red, green, blue);
                                if ((++xCoord) >= TOTAL_COLOURS/256*2*3) {
                                    xCoord = 0;
                                    yCoord++;
                                }
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

            Random randomGenerator = new Random();

            for (int y = 0; y < TOTAL_COLOURS/256; y++) {
                for (int x = 0; x < TOTAL_COLOURS/256*2; x++) {
                    int randomXIndex = randomGenerator.nextInt(TOTAL_COLOURS/256*2);
                    int randomYIndex = randomGenerator.nextInt(TOTAL_COLOURS/256);
                    swapColours(x, y, randomXIndex, randomYIndex);
                }
            }
        }

        /**
         * ALGORITHM: Nearest Colour to Previous Block
         * The next colour picked is the closest colour to the block to the left
         */
        private void nearestToPrevious() {
            colourCubeSlice();

            for (int i = 1; i < TOTAL_COLOURS; i++) {
                Colour targetColour = unpackColourData(buffer[i-1]);
                int nearestColour = nearestColour(targetColour, i);
                swapColours(i, nearestColour);
            }
        }

        /**
         * ALGORITHM: Nearest Colour to Block Above
         * The next colour picked is the closest colour to the block above
         */
        private void nearestToAbove() {
            colourCubeSlice();

            final int ROW_OFFSET = TOTAL_COLOURS/256*2;

            for (int i = ROW_OFFSET; i < TOTAL_COLOURS; i++) {
                Colour targetColour = unpackColourData(buffer[i-ROW_OFFSET]);
                int nearestColour = nearestColour(targetColour, i);
                swapColours(i, nearestColour);
            }
        }

        /**
         * ALGORITHM: Nearest Colour to Both Above and Previous Blocks
         * The next colour picked is the closest colour to the average of the blocks to the left and above
         */
        private void nearestToAboveAndPrevious() {
            colourCubeSlice();

            final int ROW_OFFSET = TOTAL_COLOURS/256*2;

            for (int i = ROW_OFFSET; i < TOTAL_COLOURS; i++) {
                Colour targetColourAbove = unpackColourData(buffer[i-ROW_OFFSET]);
                Colour targetColourPrevious = unpackColourData(buffer[i-1]);
                Colour targetColour = new Colour(targetColourAbove, targetColourPrevious);
                int nearestColour = nearestColour(targetColour, i);
                swapColours(i, nearestColour);
            }
        }

        /**
         * ALGORITHM: Nearest Colour to All Three Blocks Above
         * The next colour picked is the closest colour to the average of all 3 blocks above
         */
        private void nearestToAllThreeAbove() {
            colourCubeSlice();

            final int ROW_OFFSET = TOTAL_COLOURS/256*2;

            for (int i = ROW_OFFSET + 1; i < TOTAL_COLOURS; i++) {
                Colour targetColourAbove = unpackColourData(buffer[i-ROW_OFFSET]);
                Colour targetColourAboveLeft = unpackColourData(buffer[i-ROW_OFFSET-1]);
                Colour targetColourAboveRight = unpackColourData(buffer[i-ROW_OFFSET+1]);
                Colour mixColour = new Colour(targetColourAbove, targetColourAboveLeft);
                Colour targetColour = new Colour(targetColourAboveRight, mixColour);
                int nearestColour = nearestColour(targetColour, i);
                swapColours(i, nearestColour);
            }
        }

        /**
         * ALGORITHM: Nearest Colour to All Three Pixels Above Plus Previous
         * The next colour picked is the closest colour to the average of all 3 blocks above and the one to the left
         */
        private void nearestToAllThreeAboveAndPrevious() {
            colourCubeSlice();

            final int ROW_OFFSET = TOTAL_COLOURS/256*2;

            for (int i = ROW_OFFSET + 1; i < TOTAL_COLOURS; i++) {
                Colour targetColourPrevious = unpackColourData(buffer[i-1]);
                Colour targetColourAbove = unpackColourData(buffer[i-ROW_OFFSET]);
                Colour targetColourAboveLeft = unpackColourData(buffer[i-ROW_OFFSET-1]);
                Colour targetColourAboveRight = unpackColourData(buffer[i-ROW_OFFSET+1]);
                Colour mixColour = new Colour(targetColourAbove, targetColourAboveLeft);
                Colour mixColour2 = new Colour(targetColourAboveRight, mixColour);
                Colour targetColour = new Colour(targetColourPrevious, mixColour2);
                int nearestColour = nearestColour(targetColour, i);
                swapColours(i, nearestColour);
                if (i % 10 == 0)
                    Log.i("sd", String.format("%d",i));

            }
            publishProgress(TOTAL_COLOURS);
        }

        /**
         * Packs the data for a since ABGR format pixel
         * @param red the red value
         * @param green the green value
         * @param blue the blue value
         * @return 32 bit packed data
         */
        private int packColourData(int red, int green, int blue) {
            return (0xff << 24) | (blue & 0xff) << 16 | (green & 0xff) << 8 | (red & 0xff);
        }

        /**
         * Unpacks the data into the RGB components
         * @param packedData the 32 bit packed ABGR pixel
         * @return the colour
         */
        private Colour unpackColourData(int packedData) {
            Colour colour = new Colour(
                    packedData & 0xff,
                    (packedData >> 8) & 0xff,
                    (packedData >> 16) & 0xff
            );
            return colour;
        }

        /**
         * Swaps 2 colours in the buffer
         * @param x1 x coordinate of colour 1
         * @param y1 y coordinate of colour 1
         * @param x2 x coordinate of colour 2
         * @param y2 coordinate off colour 2
         */
        private void swapColours(int x1, int y1, int x2, int y2) {
            int tempColour = buffer[y1*TOTAL_COLOURS/256*2+x1];
            buffer[y1*TOTAL_COLOURS/256*2+x1] = buffer[y2*TOTAL_COLOURS/256*2+x2];
            buffer[y2*TOTAL_COLOURS/256*2+x2] = tempColour;
        }

        /**
         * Swaps 2 colours in the buffer
         * @param i1 index colour 1
         * @param i2 index of colour 2
         */
        private void swapColours(int i1, int i2) {
            int tempColour = buffer[i1];
            buffer[i1] = buffer[i2];
            buffer[i2] = tempColour;
        }

        /**
         * Finds nearest colour to target
         * @param targetColour colour you want the nearest colour to
         * @param searchIndex index of the beginning of the search
         * @return index of nearest colour
         */
        private int nearestColour(Colour targetColour, int searchIndex) {
            int minDistance = Integer.MAX_VALUE;
            int closestIndex = searchIndex;

            for (int i = searchIndex; i < TOTAL_COLOURS; i += 2) {
                // Calculates distance in colour cube by pythagoras but omitting the square root step to improve performance

                Colour currentColour = unpackColourData(buffer[i]);

                int redComponent = currentColour.getRed() - targetColour.getRed();
                int greenComponent = currentColour.getGreen() - targetColour.getGreen();
                int blueComponent = currentColour.getBlue() - targetColour.getBlue();
                int distance = redComponent * redComponent + greenComponent * greenComponent + blueComponent * blueComponent;
                if (distance < minDistance) {
                    closestIndex = i;
                    minDistance = distance;
                }
            }

            return closestIndex;
        }
    }
    /**
     * Total number of colours = 32^3
     */
    private final int TOTAL_COLOURS = 32768;

    /**
     * Graphics buffer
     */
    private int[] buffer;

    private AlgorithmType algorithm;

    /**
     * Constructor
     */
    public ColourScreen(AlgorithmType algorithm) {
        buffer = new int[TOTAL_COLOURS];
        this.algorithm = algorithm;
        processImageTask = new ProcessImageTask();
    }

    public void processImage() {
        processImageTask.execute(algorithm);
    }

    private void generateBitmap() {
        if (generatedBitmap != null)
            generatedBitmap.recycle();
        generatedBitmap = Bitmap.createBitmap(buffer, TOTAL_COLOURS/256*2, TOTAL_COLOURS/256, Bitmap.Config.ARGB_8888);
    }

    /**
     * Main drawing method
     */
    public Bitmap getBitmap(Matrix scalingMatrix) {
        if (generatedBitmap == null) {
            generateBitmap();
        }

        if (resizedBitmap != null)
            resizedBitmap.recycle();

        resizedBitmap = Bitmap.createBitmap(generatedBitmap, 0, 0, 256, 128, scalingMatrix, false);

        return resizedBitmap;
    }
}