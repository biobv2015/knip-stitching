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
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

public class PairwiseStitchingTest<T extends RealType<T>, C extends ComplexType<C>>
        extends AbstractOpTest {

    @SuppressWarnings("unchecked")
    // @Test
    public void testMin() throws ImgIOException {

        // ImgOpener opener = new ImgOpener();
        //
        // ImgPlus<T> imp1 = (SCIFIOImgPlus<T>) opener
        // .openImgs("res/testimgs/square0.ome.tif").get(0);
        // ImgPlus<T> imp2 = (SCIFIOImgPlus<T>) opener
        // .openImgs("res/testimgs/square0.ome.tif").get(0);
        //
        // StitchingParameters params = new StitchingParameters();
        // params.fusionMethod = FusionType.MIN_INTENSITY;
        //
        // RandomAccessibleInterval<T> outimg = PairwiseStitching
        // .performPairWiseStitching(imp1, imp2, params, ops);
        // ImageJFunctions.show(outimg);
        // System.out.println("blub");

    }

    @SuppressWarnings("unchecked")
    // @Test
    public void testMax() throws ImgIOException {
        ImgOpener opener = new ImgOpener();

        ImgPlus<T> imp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/square0.ome.tif").get(0);
        ImgPlus<T> imp2 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/square0.ome.tif").get(0);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.MAX_INTENSITY;

        RandomAccessibleInterval<T> outImg = PairwiseStitching
                .performPairWiseStitching(imp1, imp2, params, ops);
        ImageJFunctions.show(outImg);
        System.out.println("blub");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fFTTest() throws ImgIOException {
        // ImgOpener opener = new ImgOpener();
        //
        // ImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
        // .openImgs("res/testimgs/img3-1.png.ome.tif").get(0);
        //
        // RandomAccessibleInterval<C> fft1 = (RandomAccessibleInterval<C>) ops
        // .filter().fft(null, sfimp1, null, true);
        //
        // ImageJFunctions.show(sfimp1);

        System.out.println("");
    }

}
