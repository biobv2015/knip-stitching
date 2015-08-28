package org.knime.knip.stitching;

import org.junit.Test;
import org.knime.knip.stitching.lib.PairwiseStitching;
import org.knime.knip.stitching.lib.StitchingParameters;
import org.knime.knip.stitching.util.AbstractOpTest;
import org.knime.knip.stitching.util.FusionType;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;

public class PairwiseStitchingTest<T extends RealType<T>>
        extends AbstractOpTest {

    @SuppressWarnings("unchecked")
    // @Test
    public void testAverage() throws ImgIOException {

        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img3-1.png.ome.tif").get(0);
        ImgPlus<T> sfimp2 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img3-2.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.AVERAGE;

        PairwiseStitching.performPairWiseStitching(imp1, imp2, params, ops);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMin() throws ImgIOException {

        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/square0.ome.tif").get(0);
        ImgPlus<T> sfimp2 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img3-2.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp1);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.MIN_INTENSITY;

        RandomAccessibleInterval<T> outimg = PairwiseStitching
                .performPairWiseStitching(imp1, imp2, params, ops);
        ImageJFunctions.show(outimg);
        System.out.println("blub");

    }

    @SuppressWarnings("unchecked")
    // @Test
    public void testMax() throws ImgIOException {
        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img3-1.png.ome.tif").get(0);
        ImgPlus<T> sfimp2 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img3-1.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.MAX_INTENSITY;

        RandomAccessibleInterval<T> outImg = PairwiseStitching
                .performPairWiseStitching(imp1, imp2, params, ops);
        ImageJFunctions.show(outImg);
        System.out.println("blub");
    }

}
