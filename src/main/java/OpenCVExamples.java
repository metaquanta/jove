import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.core.Core.LINE_AA;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.video.Video.*;

/**
 * Created by matthew on 4/6/14.
 */
public class OpenCVExamples {
    // Source: https://gist.github.com/b95505017/6862032
    static int last = 0;
    // number of cyclic frame buffer used for motion detection
    // (should, probably, depend on FPS)
    static final int N = 4;
    static final double MHI_DURATION = 1;
    static final double MAX_TIME_DELTA = 0.5;
    static final double MIN_TIME_DELTA = 0.05;
    static Mat mhi, orient, mask, segmask;
    static Mat[] buf;
    static double magnitude, startTime = 0;
    static final int diff_threshold = 30;
    public static Mat update_mhi(Mat img) {
        Size size = new Size(img.width(), img.height());
        if(buf == null) {
            buf = new Mat[N];
            for (int i = 0; i < N; i++) {
                buf[i] = Mat.zeros(size, CvType.CV_8UC1);
            }
            mhi = Mat.zeros(size, CvType.CV_32FC1);
            segmask = Mat.zeros(size, CvType.CV_32FC1);
            mask = Mat.zeros(size, CvType.CV_8UC1);
            orient = Mat.zeros(size, CvType.CV_32FC1);
            startTime = System.nanoTime();

        }
        Mat dst = Mat.zeros(size, CvType.CV_8UC3);
        double timestamp = (System.nanoTime() - startTime) / 1e9;
        int idx1 = last, idx2;
        Mat silh;
        cvtColor(img, buf[last], COLOR_BGR2GRAY);
        double angle, count;

        idx2 = (last + 1) % N; // index of (last - (N-1))th frame
        last = idx2;

        silh = buf[idx2];
        absdiff(buf[idx1], buf[idx2], silh);
        threshold(silh, silh, diff_threshold, 1, THRESH_BINARY);
        updateMotionHistory(silh, mhi, timestamp, MHI_DURATION);
        mhi.convertTo(mask, mask.type(), 255.0 / MHI_DURATION,
                (MHI_DURATION - timestamp) * 255.0 / MHI_DURATION);
        dst.setTo(new Scalar(0));
        List<Mat> list = new ArrayList<Mat>(3);
        list.add(mask);
        list.add(Mat.zeros(mask.size(), mask.type()));
        list.add(Mat.zeros(mask.size(), mask.type()));
        merge(list, dst);
        calcMotionGradient(mhi, mask, orient, MAX_TIME_DELTA, MIN_TIME_DELTA, 3);
        MatOfRect roi = new MatOfRect();
        segmentMotion(mhi, segmask, roi, timestamp, MAX_TIME_DELTA);
        int total = roi.toArray().length;
        Rect[] rois = roi.toArray();
        Rect comp_rect;
        Scalar color;
        for (int i = -1; i < total; i++) {
            if (i < 0) {
                comp_rect = new Rect(0, 0, img.width(), img.height());
                color = new Scalar(255, 255, 255);
                magnitude = 100;
            } else {
                comp_rect = rois[i];
                if (comp_rect.width + comp_rect.height < 100) // reject very small components
                    continue;
                color = new Scalar(0, 0, 255);
                magnitude = 30;
            }

            Mat silhROI = silh.submat(comp_rect);
            Mat mhiROI = mhi.submat(comp_rect);
            Mat orientROI = orient.submat(comp_rect);
            Mat maskROI = mask.submat(comp_rect);

            angle = calcGlobalOrientation(orientROI, maskROI, mhiROI, timestamp, MHI_DURATION);
            angle = 360.0 - angle;
            count = Core.norm(silhROI, NORM_L1);

            silhROI.release();
            mhiROI.release();
            orientROI.release();
            maskROI.release();
            if (count < comp_rect.height * comp_rect.width * 0.05) {
                continue;
            }
            Point center = new Point((comp_rect.x + comp_rect.width / 2),
                    (comp_rect.y + comp_rect.height / 2));
            circle(dst, center, (int) Math.round(magnitude * 1.2), color, 3, LINE_AA, 0);
            Core.line(dst, center, new Point(
                    Math.round(center.x + magnitude * Math.cos(angle * Math.PI / 180)),
                    Math.round(center.y - magnitude * Math.sin(angle * Math.PI / 180))), color, 3, LINE_AA, 0);
        }

        return dst;
    }
}
