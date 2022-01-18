package org.firstinspires.ftc.teamcode.pipelines;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Config
public class TSEpipeline extends OpenCvPipeline {

    public static double lowerValues[] = {0, 0, 0};
    public static double upperValues[] = {255, 120, 255};


    private Mat mat = new Mat();
    private Mat ret = new Mat();

    private Rect maxRect = new Rect();
    public static int HORIZON = 110;
    public static double minRatio = 0.5;



    private double TSEx = 0.0;

    public enum TSEpos {
        LEFT,
        MIDDLE,
        RIGHT
    }
    public static double posThresholds[] = {106, 212};

    @Override
    public Mat processFrame(Mat input) {
        ret.release();
        Scalar lower = new Scalar(lowerValues[0],lowerValues[1],lowerValues[2]);
        Scalar upper = new Scalar(upperValues[0], upperValues[1], upperValues[2]);

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2YCrCb);
        Mat mask = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1); // variable to store mask in
        Core.inRange(mat, lower, upper, mask);


        Core.bitwise_and(input, input, ret, mask);

        Imgproc.GaussianBlur(mask, mask, new Size(5.0, 15.0), 0.00);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);


        Log.i("contours", contours.toString());
        //Imgproc.drawContours(ret, contours, contours.lastIndexOf(contours), new Scalar(0.0, 255.0, 0.0), 3);
        int maxWidth = 0;
        for (MatOfPoint c : contours) {
            MatOfPoint2f copy = new MatOfPoint2f(c.toArray());
            Rect rect = Imgproc.boundingRect(copy);

            int w = rect.width;
            // checking if the rectangle is below the horizon
            if (w > maxWidth && (rect.y > HORIZON) && ((rect.height / rect.width) > minRatio)) {
                maxWidth = w;
                maxRect = rect;
            }
            c.release(); // releasing the buffer of the contour, since after use, it is no longer needed
            copy.release(); // releasing the buffer of the copy of the contour, since after use, it is no longer needed
        }
        Imgproc.rectangle(ret, maxRect, new Scalar(0.0, 0.0, 255.0), 2);
        Imgproc.line(
                ret,
                new Point(
                        .0,
                        (double) HORIZON
                ),
                new Point(
                        320.0,
                        (double) HORIZON
                ),
                new Scalar(
                        255.0,
                        .0,
                        255.0)
        );

        TSEx = maxRect.x;
        mat.release();
        mask.release();
        hierarchy.release();
        return ret;
    }

    public TSEpos getTSEpos() {
        if (TSEx > 0 && TSEx < posThresholds[0]) {
            return TSEpos.LEFT;
        } else if (TSEx > posThresholds[0] && TSEx < posThresholds[1]) {
            return TSEpos.MIDDLE;
        } else {
            return TSEpos.RIGHT;
        }
    }
}
