import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvMerge;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvEqualizeHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Gray2BinImage {
	
	public IplImage convert_gray2bin(IplImage src_img){
		IplImage img=null,rgb_img=null,hsv_img=null,bin_img=null;
		
		bin_img=cvCreateImage(cvGetSize(src_img), 8, 1);
		img=cvCreateImage(cvGetSize(src_img), 8, 3);
		hsv_img=cvCreateImage(cvGetSize(src_img), 8, 3);
		
		
		//cvMerge(src_img, src_img, src_img, null, img);
		//cvCvtColor(img, hsv_img, CV_BGR2HSV);
		
		//cvInRangeS(img, cvScalar(83, 124, 0, 0), cvScalar(179, 255, 255, 0), bin_img);
		
		cvMerge(null, null, src_img, null, img);
		//cvCvtColor(img, hsv_img, CV_BGR2HSV);
		
		cvInRangeS(img, cvScalar(0, 0, 0, 0), cvScalar(0, 0, 60, 0), bin_img);
		
		
		return bin_img;
	}

}
