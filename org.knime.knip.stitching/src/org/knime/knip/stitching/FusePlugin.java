package org.knime.knip.stitching;

import org.knime.knip.stitching.lib.PairwiseStitching;
import org.knime.knip.stitching.util.FusionType;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.img.ImgView;
import net.imglib2.type.numeric.RealType;

@Plugin(menu = { @Menu(label = "DeveloperPlugins"),
        @Menu(label = "Fuse Plugin") }, description = "Fuse Plugin",
        headless = true, type = Command.class)
public class FusePlugin<T extends RealType<T>> implements Command {

    @Parameter(type = ItemIO.INPUT, label = "Image 1")
    private ImgPlus<T> input1;

    @Parameter(type = ItemIO.INPUT, label = "Image 2")
    private ImgPlus<T> input2;

    @Parameter(type = ItemIO.INPUT, label = "x-Offset")
    private long xOffset;

    @Parameter(type = ItemIO.INPUT, label = "y-Offset")
    private long yOffset;

    @Parameter(type = ItemIO.INPUT, label = "Fusion Type",
            choices = { FusionType.MAX_INTENSITY, FusionType.MIN_INTENSITY, })
    private String fusionMethod = FusionType.MAX_INTENSITY;

    @Parameter(type = ItemIO.INPUT)
    private OpService ops;

    @Parameter(type = ItemIO.OUTPUT)
    private ImgPlus<T> output;

    @Override
    public void run() {

        long[] offset = { xOffset, yOffset };

        output = new ImgPlus<T>(
                ImgView.wrap(PairwiseStitching.fuse(fusionMethod, input1,
                        input2, offset, ops), input1.factory()),
                input1);
    }
}
