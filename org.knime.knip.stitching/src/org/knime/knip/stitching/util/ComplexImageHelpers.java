package org.knime.knip.stitching.util;

import org.knime.knip.stitching.lib.PairWiseStitchingImgLib;

import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.complex.ComplexFloatType;

public class ComplexImageHelpers {

    public static final <T extends ComplexFloatType> void normalizeComplexImage(
            final Img<T> fftImage, final float normalizationThreshold) {
        final Cursor<T> cursor = fftImage.cursor();

        while (cursor.hasNext()) {
            cursor.next();
            normalizeLength(cursor.get(), normalizationThreshold);
        }
    }

    public static final <T extends ComplexFloatType> void normalizeAndConjugateComplexImage(
            final Img<T> fftImage, final float normalizationThreshold) {
        final Cursor<T> cursor = fftImage.cursor();

        while (cursor.hasNext()) {
            cursor.next();
            normalizeLength(cursor.get(), normalizationThreshold);
            cursor.get().complexConjugate();
        }
    }

    private static <T extends ComplexFloatType> void normalizeLength(
            final T input, final float threshold) {
        final float real = input.getRealFloat();
        final float complex = input.getRealFloat();

        final float length = (float) Math.sqrt(real * real + complex * complex);

        if (length < threshold) {
            input.setReal(0);
            input.setImaginary(0);
        } else {
            input.setReal(real / length);
            input.setImaginary(complex / length);
        }
    }

    /**
     * Calculates the
     * https://en.wikipedia.org/wiki/Spectral_density#Cross-spectral_density
     *
     * @param fft1
     *            first fft, is modified during the operation of the
     *
     * @param fft2
     *            second fft
     */
    public static <T extends ComplexFloatType> void calculateCrossPowerSpektrum(
            Img<T> fft1, Img<T> fft2) {

        normalizeComplexImage(fft1,
                PairWiseStitchingImgLib.normalizationThreshold);

        normalizeAndConjugateComplexImage(fft2,
                PairWiseStitchingImgLib.normalizationThreshold);

        // multiply the complex images
        Cursor<T> fft1cursor = fft1.cursor();
        Cursor<T> fft2RA = fft2.cursor();

        while (fft1cursor.hasNext()) {
            fft1cursor.next().mul(fft2RA.next());
        }
    }
}
