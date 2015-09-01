package org.knime.knip.stitching.lib;

import org.knime.knip.stitching.util.FusionType;

import Jama.Matrix;
import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.RealType;

public class PairwiseStitching {

    private static final float normalizationThreshold = 1E-5f;

    public static <T extends RealType<T>> RandomAccessibleInterval<T> performPairWiseStitching(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params, OpService ops) {

        long[] result = null;

        AffineGet testResult = generateTestTransform();
        // compute the stitching

        long[] offset =
                ops.filter().phaseCorrelate(imp1, imp2, normalizationThreshold);

        result = offset;

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

        return fuse(params.fusionMethod, imp1, imp2, testResult, ops);
    }

    // ========================================================================
    private static AffineGet generateTestTransform() {
        long[] peakPosition = { -30, -20 };
        int size = 2;
        double[][] translationArray = new double[size + 1][size + 2];
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                if (i == j) {
                    translationArray[i][j] = 1;
                } else if (j == size) {
                    if (peakPosition.length <= i) {
                        translationArray[i][j] = 0;
                    } else {
                        translationArray[i][j] = peakPosition[i];
                    }
                } else {
                    translationArray[i][j] = 0;
                }
            }
        }

        Matrix matrix = new Matrix(translationArray);
        AffineGet testResult = new AffineTransform(matrix);
        return testResult;
    }

    // =======================================================================

    public static <T extends RealType<T>> RandomAccessibleInterval<T> fuse(
            String fusionType, RandomAccessibleInterval<T> in1,
            RandomAccessibleInterval<T> in2, AffineGet transform,
            OpService ops) {

        RandomAccessibleInterval<T> out = null;

        int n = in1.numDimensions();
        long[] offset = new long[n];
        for (int i = 0; i < 2; i++) {
            offset[i] = (long) transform.get(i, 2);
        }

        //
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
