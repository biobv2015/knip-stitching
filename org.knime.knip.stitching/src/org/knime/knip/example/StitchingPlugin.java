package org.knime.knip.example;

import ij.ImagePlus;
import mpicbg.stitching.StitchingParameters;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import plugin.Stitching_Pairwise;
import stitching.model.TranslationModel2D;

@Plugin(menu = {@Menu(label = "DeveloperPlugins"), @Menu(label = "Stitching")}, description = "Very simple thresholder", headless = true, type = Command.class)
public class StitchingPlugin<T extends RealType<T>> implements Command {

        @Parameter(type = ItemIO.INPUT, label = "Image 1")
        private ImagePlus input1;

        @Parameter(type = ItemIO.INPUT, label = "Image 2")
        private ImagePlus input2;

        @Parameter(type = ItemIO.INPUT, label = "My Threshold")
        private double manualThreshold = 50;

        @Parameter(type = ItemIO.OUTPUT)
        private ImagePlus output;

        TranslationModel2D a = new TranslationModel2D();

        @Override
        public void run() {

                StitchingParameters params = new StitchingParameters();
                params.absoluteThreshold = manualThreshold;
                params.addTilesAsRois = false;
                params.channel1 = 0;
                params.channel2 = 0;
                params.checkPeaks = 1;
                params.computeOverlap = false;
                params.displayFusion = true;
                params.downSample = false;
                params.fusedName = "derp";
                params.fusionMethod = 0;

                output = Stitching_Pairwise.performPairWiseStitching(input1, input2, params);
        }
}
