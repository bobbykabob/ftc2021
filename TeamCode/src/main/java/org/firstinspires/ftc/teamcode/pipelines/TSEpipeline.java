package org.firstinspires.ftc.teamcode.pipelines;




import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;
import com.acmerobotics.dashboard.config.Config;
@Config
public class TSEpipeline extends OpenCvPipeline {

    public static double bluelowerValues0 = 0;
    public static double bluelowerValues1 = 0;
    public static double bluelowerValues2 = 155;
    public static double blueupperValues0 = 255;
    public static double blueupperValues1 = 90;
    public static double blueupperValues2 = 255;

    public static double redlowerValues0 = 0;
    public static double redlowerValues1 = 200;
    public static double redlowerValues2 = 0;
    public static double redupperValues0 = 150;
    public static double redupperValues1 = 255;
    public static double redupperValues2 = 100;

    public static double minArea = 200;

    private Mat mat = new Mat();
    private Mat ret = new Mat();

    private Rect maxRect = new Rect();
    public static int HORIZON = 115;
    public static double minRatio = 0.5;



    private double TSEx = 0.0;

    public enum TSEpos {
        LEFT,
        MIDDLE,
        RIGHT
    }


    //blue = 0
    //red = 1
    public static double color = 0;
    TSEpos tsepos = TSEpos.RIGHT;

    public static int posThreshold = 100;
    public static int posThreshold2 = 200;

    private Scalar lower, upper;

    @Override
    public Mat processFrame(Mat input) {
        ret.release();
        if (color == 0) {
            double lowerValues[] = {bluelowerValues0, bluelowerValues1, bluelowerValues2};
            double upperValues[] = {blueupperValues0, blueupperValues1, blueupperValues2};
            lower = new Scalar(lowerValues[0], lowerValues[1], lowerValues[2]);
            upper = new Scalar(upperValues[0], upperValues[1], upperValues[2]);
        } else if (color == 1) {
            double lowerValues[] = {redlowerValues0, redlowerValues1, redlowerValues2};
            double upperValues[] = {redupperValues0, redupperValues1, redupperValues2};
            lower = new Scalar(lowerValues[0], lowerValues[1], lowerValues[2]);
            upper = new Scalar(upperValues[0], upperValues[1], upperValues[2]);
        }

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2YCrCb);
        Mat mask = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1); // variable to store mask in
        Core.inRange(mat, lower, upper, mask);


        Core.bitwise_and(input, input, ret, mask);

        Imgproc.GaussianBlur(mask, mask, new Size(5.0, 15.0), 0.00);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);


        maxRect = new Rect(400, 0, 0, 0);
        //Imgproc.drawContours(ret, contours, contours.lastIndexOf(contours), new Scalar(0.0, 255.0, 0.0), 3);

        Scalar lineColor = new Scalar(
                255.0,
                .0,
                .0);

        Imgproc.line(
                ret,
                new Point(.0, HORIZON),
                new Point(320.0, HORIZON),
                lineColor
        );
        Imgproc.line(
                ret,
                new Point(posThreshold, HORIZON),
                new Point(posThreshold, 240.0),
                lineColor
        );
        Imgproc.line(
                ret,
                new Point(posThreshold2, HORIZON),
                new Point(posThreshold2, 240.0),
                lineColor
        );

        Rect leftRect = new Rect(0,HORIZON, posThreshold, 240 - HORIZON);
        Mat left = ret.submat(leftRect);

        Rect midRect = new Rect(posThreshold,HORIZON, posThreshold2 - posThreshold, 240 - HORIZON);
        Mat mid = ret.submat(midRect);
        Rect rightRect = new Rect(posThreshold2,HORIZON, ret.width() - posThreshold2, 240 - HORIZON);



        Mat right = ret.submat(rightRect);

        double leftval = Core.mean(left).val[1];
        double midval = Core.mean(mid).val[1];
        double rightval = Core.mean(right).val[1];

        Scalar rectColor = new Scalar(
                125.0,
                255.0,
                0.0
        );
        int thickness = 5;
        if (leftval < midval && leftval < rightval) {

            tsepos = TSEpos.LEFT;

            Imgproc.rectangle(ret,
                    leftRect,
                    rectColor, thickness);
        } else if (midval < leftval && midval < rightval) {

            tsepos = TSEpos.MIDDLE;

            Imgproc.rectangle(ret,
                    midRect,
                    rectColor, thickness);
        } else {
            tsepos = TSEpos.RIGHT;

            Imgproc.rectangle(ret,
                    rightRect,
                    rectColor, thickness);
        }

        Imgproc.putText(ret, Double.toString( Math.floor(100 *leftval) / 100), new Point(0, HORIZON-50), 1, 1, rectColor);
        Imgproc.putText(ret, Double.toString(Math.floor(100 *midval) / 100), new Point(posThreshold, HORIZON-50), 1, 1, rectColor);
        Imgproc.putText(ret, Double.toString(Math.floor(100 *rightval) / 100), new Point(posThreshold2, HORIZON-50), 1, 1, rectColor);
        Imgproc.putText(ret, tsepos.toString(), new Point(0, 50), 1, 1, rectColor);
        TSEx = maxRect.x;
        mat.release();
        mask.release();
        hierarchy.release();
        left.release();
        mid.release();
        right.release();
        return ret;
    }

    public TSEpos getTSEpos() {
       return tsepos;
    }

    public double getTSEx() {
        return TSEx;
    }
}
