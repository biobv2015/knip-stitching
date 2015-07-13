package org.knime.knip.stitching.lib;

import java.util.ArrayList;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class PairwiseStitching {

    public static <T extends RealType<T>> ImgPlus<T> performPairWiseStitching(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params, OpService ops) {

        PairWiseStitchingResult result = null;
        // the simplest case, only one registration necessary
        if (imp1.dimension(params.channel1) == 1 || params.timeSelect == 0) {
            result = singleTimepointStitching(imp1, imp2, params, ops);
        } else {
            // multiTimepointStitching(imp1, imp2, params, models, ops);
            // TODO: IMPLEMENT
        }
        return fuseImg(imp1, imp2, params, result, ops);
    }

    private static <T extends RealType<T>> PairWiseStitchingResult singleTimepointStitching(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params, OpService opservice) {
        // compute the stitching
        final long start = System.currentTimeMillis();

        final PairWiseStitchingResult result;

        // Always compute overlap!
        result = PairWiseStitchingImgLib.stitchPairwise(imp1, imp2, 1, 1,
                params, opservice);

        return result;
    }

    /**
     * Fuses an Image selecting the right parameters for
     *
     * @param imp1
     * @param imp2
     * @param params
     * @param result
     * @return
     */
    private static <T extends RealType<T>> ImgPlus<T> fuseImg(
            final ImgPlus<T> imp1, final ImgPlus<T> imp2,
            final StitchingParameters params,
            final PairWiseStitchingResult result, OpService ops) {

        final long start = System.currentTimeMillis();
        final ImgPlus<T> resultImg = fuse(imp1, imp2, params, result, ops);

        return resultImg;
    }

    @SuppressWarnings("deprecation")
    private static <T extends RealType<T>> ImgPlus<T> fuse(
            final ImgPlus<T> img1, final ImgPlus<T> img2,
            final StitchingParameters params, PairWiseStitchingResult result,
            OpService ops) {
        final ArrayList<ImgPlus<T>> images = new ArrayList<ImgPlus<T>>();
        images.add(img1);
        images.add(img2);

        long[] offset = result.getOffset();

        long[] outImgsize = new long[img1.numDimensions()];
        for (int i = 0; i < img1.numDimensions(); i++) {
            outImgsize[i] = img1.dimension(i) + Math.abs(offset[i]);
        }

        RandomAccess<T> img1RA = img1.randomAccess();
        RandomAccess<T> img2RA = Views.offset(img2, offset).randomAccess();
        FinalInterval outInterval =
                Intervals.createMinMax(0, 0, outImgsize[0], outImgsize[1]); // fixme

        T outType = img1.firstElement().createVariable();

        outType.setOne();
        outType.add(outType);

        Img<T> outImg = ops.create().img(outInterval, outType);

        Cursor<T> outcursor = outImg.localizingCursor();
        long[] tempPos = new long[outImg.numDimensions()];
        while (outcursor.hasNext()) {
            outcursor.fwd();
            outcursor.localize(tempPos);
            img1RA.setPosition(tempPos);
            img2RA.setPosition(tempPos);

            T i1 = img1RA.get();
            T i2 = img2RA.get();
            i1.mul(i2);
            i1.div(outType);
            outcursor.get().set(i1);
        }
        ImageJFunctions.show(outImg);
        return ImgPlus.wrap(outImg);
    }

    // T a = offsetimg2.get();
    // T b = origRA.get();
    //
    // System.out.println(a.compareTo(b));

    // if (params.fusionMethod != FusionType.OVERLAY
    // || params.fusionMethod != FusionType.INTENSITY_RANDOM_TILE) {
    // final ImagePlus imp = Fusion.fuse(targetType, images, models,
    // params.dimensionality, params.subpixelAccuracy,
    // params.fusionMethod, null, false,
    // params.ignoreZeroValuesFusion, params.displayFusion);
    // return imp;
    // } else {
    // // "Do not fuse images"
    // return null;

}
