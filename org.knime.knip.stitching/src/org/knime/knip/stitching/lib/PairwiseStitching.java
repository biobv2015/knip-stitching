package org.knime.knip.stitching.lib;

import org.knime.knip.stitching.fusion.Fusion;

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

        AffineGet result = null;

        AffineGet testResult = generateTestTransform();

        // FIXME check the if conditions
        /*
         * int relevantDim = imp1.dimensionIndex(Axes.TIME); if (relevantDim ==
         * -1) { return null; }
         */
        // the simplest case, only one registration necessary
        // if (relevantDim < 2) {
        result = singleTimepointStitching(imp1, imp2, params, ops);
        // } else {
        // multiTimepointStitching(imp1, imp2, params, models, ops);
        // TODO: IMPLEMENT
        // }

        return Fusion.fuse(params.fusionMethod, imp1, imp2, testResult, ops);
    }

    // ========================================================================
    // TODO compare this method with filling of phasecorrelate op before removal
    private static AffineGet generateTestTransform() {
        long[] peakPosition = { 0, 200 };
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

    private static <T extends RealType<T>> AffineGet singleTimepointStitching(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params, OpService ops) {
        // compute the stitching
        final long start = System.currentTimeMillis();

        // Always compute overlap!
        // result = PairWiseStitchingImgLib.stitchPairwise(imp1, imp2, 1, 1,
        // params, opservice);

        AffineGet affineTransform =
                ops.filter().phaseCorrelate(imp1, imp2, normalizationThreshold);

        affineTransform.toString();

        // result = computePhaseCorrelation(imp1, imp2, params, ops);

        return affineTransform;
    }

}
