package org.knime.knip.stitching.lib;

public class StitchingParameters {

    /**
     * If you want to force that the {@link ContainerFactory} above is always
     * used set this to true
     */

    public String fusionMethod;
    public int checkPeaks = 5;
    public boolean subpixelAccuracy;

    // Globaloptimization
    public double regThreshold = -2;
    public double relativeThreshold = 2.5;
    public double absoluteThreshold = 3.5;

}
