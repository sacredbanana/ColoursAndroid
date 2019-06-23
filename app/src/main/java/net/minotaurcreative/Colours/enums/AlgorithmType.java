package net.minotaurcreative.Colours.enums;

public enum AlgorithmType {
    COLOUR_CUBE_SLICE,
    COLOUR_CUBE_SLICE_WITH_SMOOTHING,
    RANDOM_SPREAD,
    NEAREST_TO_PREVIOUS_COLOUR,
    COLOUR_CUBE_SLICE_WITH_RED_ACCUMULATION,
    NEAREST_TO_BLOCK_ABOVE,
    NEAREST_TO_BOTH_ABOVE_AND_PREVIOUS_BLOCKS,
    NEAREST_TO_ALL_THREE_BLOCKS_ABOVE,
    NEAREST_TO_ALL_THREE_PIXELS_ABOVE_PLUS_PREVIOUS;

    public static String name(AlgorithmType value) {
        switch (value) {
            case COLOUR_CUBE_SLICE:
                return "Colour Cube Slice";
            case COLOUR_CUBE_SLICE_WITH_SMOOTHING:
                return "Colour Cube Slice with Smoothing";
            case RANDOM_SPREAD:
                return "Random Spread";
            case COLOUR_CUBE_SLICE_WITH_RED_ACCUMULATION:
                return "Colour Cube Slice with Red Accumulation";
            case NEAREST_TO_PREVIOUS_COLOUR:
                return "Nearest to Previous Colour";
            case NEAREST_TO_BLOCK_ABOVE:
                return "Nearest Colour to Block Above";
            case NEAREST_TO_BOTH_ABOVE_AND_PREVIOUS_BLOCKS:
                return "Nearest Colour to Both Above and Previous Blocks";
            case NEAREST_TO_ALL_THREE_BLOCKS_ABOVE:
                return "Nearest Colour to All Three Blocks Above";
            case NEAREST_TO_ALL_THREE_PIXELS_ABOVE_PLUS_PREVIOUS:
                return "Nearest Colour to All Three Pixels Above Plus Previous";
            default:
                return "";
        }
    }
}
