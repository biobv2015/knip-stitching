package org.knime.knip.stitching.lib;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.type.numeric.RealType;

/**
 * Pairwise Stitching of two ImagePlus using ImgLib1 and PhaseCorrelation. It
 * deals with aligning two slices (2d) or stacks (3d) having an arbitrary amount
 * of channels. If the ImagePlus contains several time-points it will only
 * consider the first time-point as this requires global optimization of many
 * independent 2d/3d <-> 2d/3d alignments.
 *
 * @author Stephan Preibisch (stephan.preibisch@gmx.de)
 *
 */
public class PairWiseStitchingImgLib {

    public static final float normalizationThreshold = 1E-5f;

    public static <T extends RealType<T>> PairWiseStitchingResult stitchPairwise(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2, final int timepoint1,
            final int timepoint2, final StitchingParameters params,
            OpService ops) {
        PairWiseStitchingResult result = null;

        AffineGet affineTransform =
                ops.filter().phaseCorrelate(imp1, imp2, normalizationThreshold);

        affineTransform.toString();

        // result = computePhaseCorrelation(imp1, imp2, params, ops);

        if (result == null) {
            // Log.error("Pairwise stitching failed.");
            return null;
        }
        // add the offset to the shift
        // result.offset[0] -= imp2.max(0);
        // result.offset[1] -= imp2.max(1);
        //
        // result.offset[0] += imp1.max(1);
        // result.offset[1] += imp1.max(1);

        return result;
    }
}
