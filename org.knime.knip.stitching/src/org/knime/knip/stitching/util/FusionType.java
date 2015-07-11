package org.knime.knip.stitching.util;

/**
 * Available Fusion Methods
 */
public class FusionType {
    public static final String OVERLAY = "Overlay into Composite Image";
    public static final String LINEAR_BLENDING = "Linear Blending";
    public static final String AVERAGE = "Average Intensity";
    public static final String MEDIAN = "Median Intensity";
    public static final String MAX_INTENSITY = "Max Intensity";
    public static final String MIN_INTENSITY = "Min Intensity";
    public static final String INTENSITY_RANDOM_TILE =
            "Intensity of Random Input Tile";
    public static final String NO_FUSE = "Do not fuse Images";
}
