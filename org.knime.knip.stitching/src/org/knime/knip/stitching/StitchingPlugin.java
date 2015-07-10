package org.knime.knip.stitching;

import org.knime.knip.stitching.lib.Stitching_Pairwise;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;

@Plugin(menu = { @Menu(label = "DeveloperPlugins"),
        @Menu(label = "Stitching") }, description = "Very simple thresholder",
        headless = true, type = Command.class)
public class StitchingPlugin<T extends RealType<T>> implements Command {

    @Parameter(type = ItemIO.INPUT, label = "Image 1")
    private ImgPlus<T> input1;

    @Parameter(type = ItemIO.INPUT, label = "Image 2")
    private ImgPlus<T> input2;

    @Parameter(type = ItemIO.INPUT, label = "Check Peaks")
    private int checkPeaks = 200;

    @Parameter(type = ItemIO.INPUT, label = "Channel img 1")
    private int channelImg1 = 0;

    @Parameter(type = ItemIO.INPUT, label = "Channel img 2")
    private int channelImg2 = 0;

    @Parameter(type = ItemIO.INPUT, label = "Add Tiles as Rois")
    private boolean addTilesAsRois = false;

    @Parameter(type = ItemIO.INPUT, label = "Compute Overlap")
    private boolean computeOverlap = true;

    @Parameter(type = ItemIO.INPUT, label = "Down Sample")
    private boolean downSample = false;

    @Parameter(type = ItemIO.INPUT, label = "Name of Fused Image")
    private String fusedName = "Fused";

    @Parameter(type = ItemIO.INPUT, label = "subPixel accuracy")
    private boolean subPixelAccuracy = true;

    @Parameter(type = ItemIO.INPUT, label = "Dimensionality")
    private int dimensionality = 2;

    @Parameter(type = ItemIO.INPUT)
    private OpService ops;

    @Parameter(type = ItemIO.INPUT, label = "Fusion Type",
            choices = { FusionType.AVERAGE, FusionType.LINEAR_BLENDING,
                    FusionType.MAX_INTENSITY, FusionType.MEDIAN,
                    FusionType.MIN_INTENSITY, FusionType.NO_FUSE,
                    FusionType.OVERLAY })
    private String fusionMethod = FusionType.AVERAGE;

    @Parameter(type = ItemIO.OUTPUT)
    private ImgPlus<T> output;

    @Override
    public void run() {

        StitchingParameters params = new StitchingParameters();

        // from user
        params.addTilesAsRois = addTilesAsRois;
        params.checkPeaks = checkPeaks;
        params.downSample = downSample;
        params.fusedName = fusedName;
        params.fusionMethod = fusionMethod;
        params.subpixelAccuracy = subPixelAccuracy;
        params.dimensionality = dimensionality;

        // required
        params.computeOverlap = true;
        // always use all channels
        params.channel1 = 0;
        params.channel2 = 0;

        output = Stitching_Pairwise.performPairWiseStitching(input1, input2,
                params, ops);
    }
}
