package org.knime.knip.stitching;

import org.junit.Test;
import org.knime.knip.stitching.lib.StitchingParameters;
import org.knime.knip.stitching.lib.Stitching_Pairwise;
import org.knime.knip.stitching.util.AbstractOpTest;
import org.scijava.Context;
import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;

import io.scif.SCIFIOService;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;

public class PairwiseStitchingTest<T extends RealType<T>>
        extends AbstractOpTest {

    @Parameter
    OpService ops;

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws ImgIOException {
        StitchingParameters params = new StitchingParameters();

        Context context = new Context(SCIFIOService.class, StatusService.class);
        ImgOpener opener = new ImgOpener(context);

        SCIFIOImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img1-1.jpg").get(0);
        SCIFIOImgPlus<T> sfimp2 = (SCIFIOImgPlus<T>) opener
                .openImgs("res/testimgs/img1-2.jpg").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        Stitching_Pairwise.performPairWiseStitching(imp1, imp2, params, ops);
    }

}
