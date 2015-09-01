package org.knime.knip.stitching.lib;

import org.knime.knip.stitching.util.FusionType;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

public class PairwiseStitching {

    private static final float normalizationThreshold = 1E-5f;

    public static <T extends RealType<T>> RandomAccessibleInterval<T> performPairWiseStitching(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params, OpService ops) {

        // compute the stitching
        long[] offset =
                ops.filter().phaseCorrelate(imp1, imp2, normalizationThreshold);

        // FIXME check the if conditions
        /*
         * int relevantDim = imp1.dimensionIndex(Axes.TIME); if (relevantDim ==
         * -1) { return null; }
         */
        // the simplest case, only one registration necessary
        // if (relevantDim < 2) {

        // } else {
        // multiTimepointStitching(imp1, imp2, params, models, ops);
        // TODO: IMPLEMENT
        // }

        return fuse(params.fusionMethod, imp1, imp2, offset, ops);
    }

    public static <T extends RealType<T>> RandomAccessibleInterval<T> fuse(
            String fusionType, RandomAccessibleInterval<T> in1,
            RandomAccessibleInterval<T> in2, long[] offset, OpService ops) {

        RandomAccessibleInterval<T> out = null;

        if (FusionType.MIN_INTENSITY.equals(fusionType)) {
            out = ops.image().fuseMin(in1, in2, offset);
        } else if (FusionType.MAX_INTENSITY.equals(fusionType)) {
            out = ops.image().fuseMax(in1, in2, offset);
        } else {
            throw new IllegalArgumentException(
                    fusionType + " is not an implemented fusion type!");
        }

        return out;
    }

}
