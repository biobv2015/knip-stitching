package org.knime.knip.stitching.lib;

import java.util.ArrayList;

import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccess;
import net.imglib2.type.numeric.RealType;

public class Stitching_Pairwise {
    final private String myURL = "http://fly.mpi-cbg.de/preibisch";
    final private String paperURL =
            "http://bioinformatics.oxfordjournals.org/cgi/content/abstract/btp184";

    public static int defaultImg1 = 0;
    public static int defaultImg2 = 1;
    public static int defaultChannel1 = 0;
    public static int defaultChannel2 = 0;
    public static int defaultTimeSelect = 1;
    public static boolean defaultFuseImages = true;
    public static int defaultFusionMethod = 0;
    public static boolean defaultIgnoreZeroValues = false;
    public static boolean defaultComputeOverlap = true;
    public static boolean defaultSubpixelAccuracy = false;
    public static int defaultCheckPeaks = 5;
    public static double defaultxOffset = 0, defaultyOffset = 0,
            defaultzOffset = 0;

    public static boolean[] defaultHandleChannel1 = null;
    public static boolean[] defaultHandleChannel2 = null;

    public static int defaultMemorySpeedChoice = 0;
    public static double defaultDisplacementThresholdRelative = 2.5;
    public static double defaultDisplacementThresholdAbsolute = 3.5;

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

    private static <T extends RealType<T>> ImgPlus<T> fuse(
            final ImgPlus<T> img1, final ImgPlus<T> img2,
            final StitchingParameters params, PairWiseStitchingResult result,
            OpService ops) {
        final ArrayList<ImgPlus<T>> images = new ArrayList<ImgPlus<T>>();
        images.add(img1);
        images.add(img2);

        RandomAccess<T> origRA = img2.randomAccess();
        // origRA.setPosition(new int[]{10,10}};

        // RandomAccess<T> offsetimg2 = Views.offset(img2, 100,
        // 0).randomAccess();
        // offsetimg2.setPosition(10, 10);
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
        // }
        return null;
    }
}
