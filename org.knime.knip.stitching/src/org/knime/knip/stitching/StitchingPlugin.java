package org.knime.knip.stitching;

import ij.ImagePlus;
import mpicbg.stitching.plugins.Stitching_Pairwise;
import mpicbg.stitching.stitching.StitchingParameters;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(
        menu = { @Menu(label = "DeveloperPlugins"), @Menu(label = "Stitching") },
        description = "Very simple thresholder", headless = true,
        type = Command.class)
public class StitchingPlugin<T extends RealType<T>> implements Command {

    @Parameter(type = ItemIO.INPUT, label = "Image 1")
    private ImagePlus input1;

    @Parameter(type = ItemIO.INPUT, label = "Image 2")
    private ImagePlus input2;

    @Parameter(type = ItemIO.INPUT, label = "Absolute Threshold")
    private double absoluteThreshold = 5;

    @Parameter(type = ItemIO.INPUT, label = "Check Peaks")
    private int checkPeaks = 1;

    @Parameter(type = ItemIO.INPUT, label = "Fusion Method")
    private int fusionMethod = 0;

    @Parameter(type = ItemIO.INPUT, label = "Add Tiles as Rois")
    private boolean addTilesAsRois = false;

    @Parameter(type = ItemIO.INPUT, label = "Compute Overlap")
    private boolean computeOverlap = false;

    @Parameter(type = ItemIO.INPUT, label = "Down Sample")
    private boolean downSample = false;

    @Parameter(type = ItemIO.INPUT, label = "Name of Fused Image")
    private String fusedName = "Fused";

    @Parameter(type = ItemIO.OUTPUT)
    private ImagePlus output;

    @Override
    public void run() {

        StitchingParameters params = new StitchingParameters();
        params.absoluteThreshold = absoluteThreshold;
        params.addTilesAsRois = addTilesAsRois;
        params.checkPeaks = checkPeaks;
        params.computeOverlap = computeOverlap;
        // params.displayFusion = true;
        params.downSample = downSample;
        params.fusedName = fusedName;
        params.fusionMethod = fusionMethod;
        params.dimensionality = 2;

        output =
                Stitching_Pairwise.performPairWiseStitching(input1, input2,
                        params);
    }
}
