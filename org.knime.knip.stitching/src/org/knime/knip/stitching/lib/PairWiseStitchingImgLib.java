package org.knime.knip.stitching.lib;

import java.util.List;

import org.knime.knip.core.ops.img.algorithms.PhaseCorrelationPeak;
import org.knime.knip.stitching.util.ComplexImageHelpers;
import org.knime.knip.stitching.util.FixedSizePriorityQueue;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorExpWindowingFactory;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

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

    @SuppressWarnings({ "deprecation", "unchecked" })
    public static <T extends RealType<T>> PairWiseStitchingResult computePhaseCorrelation(
            final ImgPlus<T> img1, final ImgPlus<T> img2,
            StitchingParameters params, OpService ops) {

        // TODO: DO WE Need this?
        int padding = 512;
        OutOfBoundsMirrorExpWindowingFactory<T, Img<T>> mirrorPad =
                new OutOfBoundsMirrorExpWindowingFactory<T, Img<T>>(padding);

        OutOfBoundsFactory<T, Img<T>> zeroPad =
                new OutOfBoundsConstantValueFactory<T, Img<T>>(
                        Util.getTypeFromInterval(img1).createVariable());

        long[] size = new long[img1.numDimensions()];
        img1.dimensions(size);
        Img<DoubleType> outManual = ArrayImgs.doubles(size);

        Img<ComplexFloatType> fft1 =
                (Img<ComplexFloatType>) ops.filter().fft(img1.getImg());
        Img<ComplexFloatType> fft2 =
                (Img<ComplexFloatType>) ops.filter().fft(img2);

        //
        ImageJFunctions.show(fft1, "fft 1");
        ImageJFunctions.show(fft2, "fft 2");

        ComplexImageHelpers.calculateCrossPowerSpektrum(fft1, fft2);

        // return to pixelspace
        ops.filter().ifft(outManual, fft1);
        // ImageJFunctions.show(outManual, "manual");

        List<PhaseCorrelationPeak> peaks =
                extractPhaseCorrelationPeaks(outManual, params.checkPeaks, ops);
        //
        // PhaseCorrelation.verifyWithCrossCorrelation(peaks, size,
        // img1.getImg(),
        // img2.getImg());

        if (params.subpixelAccuracy) {
            // TODO: Subpixel accuracy?
        }

        PhaseCorrelationPeak topPeak = peaks.get(peaks.size() - 1);

        PairWiseStitchingResult result = new PairWiseStitchingResult(
                topPeak.getPosition(), topPeak.getPhaseCorrelationPeak(),
                topPeak.getCrossCorrelationPeak());

        return result;

    }

    /**
     * Extract the n best peaks in the phase correlation.
     *
     * @param invPCM
     *            the Inverted Phase correlation matrix
     * @param numPeaks
     *            the number of peaks to extract
     * @param ops
     *            the Opservice to use
     * @return list of the n best peaks
     */
    private static final <T extends RealType<T>> List<PhaseCorrelationPeak> extractPhaseCorrelationPeaks(
            final Img<T> invPCM, final int numPeaks, OpService ops) {

        FixedSizePriorityQueue<PhaseCorrelationPeak> peaks =
                new FixedSizePriorityQueue<PhaseCorrelationPeak>(numPeaks);
        final int dims = invPCM.numDimensions();

        ExtendedRandomAccessibleInterval<T, Img<T>> extended =
                Views.extendZero(invPCM);
        IntervalView<T> interval = Views.interval(extended, invPCM);

        // TODO: OFFSETS?

        // Define neightborhood for the Peaks
        final int neighborhoodSize = 3; // TODO
        RectangleShape rs = new RectangleShape(neighborhoodSize, false);
        Cursor<Neighborhood<T>> neighbour = rs.neighborhoods(interval).cursor();
        // find local maximum in each neighborhood
        while (neighbour.hasNext()) {
            Cursor<T> nhCursor = neighbour.next().localizingCursor();
            double maxValue = 0.0d;
            long[] maxPos = new long[dims];
            while (nhCursor.hasNext()) {
                double localValue = nhCursor.next().getRealDouble();
                if (localValue > maxValue) {
                    maxValue = localValue;
                    nhCursor.localize(maxPos);
                }
            }
            // queue ensures only n best are added.
            PhaseCorrelationPeak peak =
                    new PhaseCorrelationPeak(maxPos, (float) maxValue);
            peak.setOriginalInvPCMPosition(maxPos);
            peaks.add(peak);
        }
        return peaks.asList();
    }
}
