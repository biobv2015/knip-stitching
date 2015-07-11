package org.knime.knip.stitching.lib;

public class StitchingParameters {

    /**
     * If you want to force that the {@link ContainerFactory} above is always
     * used set this to true
     */
    public static boolean alwaysCopy = false;

    public int dimensionality;
    public String fusionMethod;
    public String fusedName;
    public int checkPeaks = 5;
    public boolean addTilesAsRois;
    public boolean computeOverlap, subpixelAccuracy,
            ignoreZeroValuesFusion = false, downSample = false,
            displayFusion = false;
    public boolean invertX, invertY;
    public boolean ignoreZStage;
    public double xOffset;
    public double yOffset;
    public double zOffset;

    public boolean virtual = false;
    public int channel1;
    /**
     * The Number of the channel dimension for the first Image
     */
    public int channel2;

    public int timeSelect;

    public int cpuMemChoice = 0;
    // 0 == fuse&display, 1 == writeToDisk
    public int outputVariant = 0;
    public String outputDirectory = null;

    public double regThreshold = -2;
    public double relativeThreshold = 2.5;
    public double absoluteThreshold = 3.5;

    // added by John Lapage: allows storage of a sequential comparison range
    public boolean sequential = false;
    public int seqRange = 1;

}