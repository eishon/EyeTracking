import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageProcessing {
	
	public float canny_threshold=255;
	
	public IplImage flip_img(IplImage image) {
		
		cvFlip(image, image, 1);
		
		return image;
	}
	
	public IplImage edge_img(IplImage image) {
		
		
		IplImage edge=cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
		
		cvSmooth( image, edge, CV_BLUR, 9, 9, 2, 2);
		cvSmooth(edge, edge, CV_GAUSSIAN, 3);
		cvThreshold(edge,edge, 155, 255, CV_THRESH_BINARY);
		cvCanny( edge, edge, canny_threshold/3, canny_threshold, 3 );
		//System.out.println(canny_threshold);
		return edge;
	}

}
