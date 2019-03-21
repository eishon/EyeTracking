import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.security.PublicKey;

import org.omg.CORBA.PUBLIC_MEMBER;

public class ColorFilter {
	
	public static float minc_h=83,minc_s=124,minc_v=0,maxc_h=179,maxc_s=255,maxc_v=255;
	
	public IplImage color_filter(IplImage src_img){
		IplImage hsv_img,bin_img;
		
		hsv_img=cvCreateImage(cvGetSize(src_img), 8, 3);
		bin_img=cvCreateImage(cvGetSize(src_img), 8, 1);
		
		
		cvCvtColor(src_img, hsv_img, CV_BGR2HSV);
		CvScalar minc=cvScalar(minc_h, minc_s, minc_v, 0), 
				maxc = cvScalar(maxc_h, maxc_s, maxc_v, 0);
		cvInRangeS(hsv_img, minc, maxc, bin_img);
		
		return bin_img;
		
	}
	
	public IplImage color_histrogram(IplImage src_img){
		IplImage hist_img,channel_1,channel_2,channel_3;
		
		hist_img=cvCreateImage(cvGetSize(src_img), 8, 3);
		channel_1=cvCreateImage(cvGetSize(src_img), 8, 1);
		channel_2=cvCreateImage(cvGetSize(src_img), 8, 1);
		channel_3=cvCreateImage(cvGetSize(src_img), 8, 1);
		
		cvCvtColor(src_img, hist_img, CV_BGR2YCrCb);
		cvSplit(hist_img, channel_1, channel_2, channel_3, null);
		cvEqualizeHist(channel_1, channel_1);
		cvMerge(channel_1, channel_2, channel_3, null, hist_img);
		cvCvtColor(hist_img, hist_img, CV_YCrCb2BGR);
		
		return hist_img;
	}
		
}