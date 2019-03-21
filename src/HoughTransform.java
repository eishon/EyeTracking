import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class HoughTransform {
	
	int cirCount=0;
	int xPos=0,yPos=0;
	int min_radius=5,max_radius=50;
	
	private CvMemStorage memory;
	
	public HoughTransform(CvMemStorage memStorage) {
		memory=memStorage;
	}
	
	IplImage HoughCircleDetection(IplImage image,IplImage src){
		
		IplImage hough=cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
		
		if(image!=null) {
			
			cvSmooth(image, hough, CV_GAUSSIAN, 3);
			   
			CvSeq circles = cvHoughCircles( 
			   hough, //Input image
			   memory, //Memory Storage
			   CV_HOUGH_GRADIENT, //Detection method
			   1, //Inverse ratio
			   100, //Minimum distance between the centers of the detected circles
			   100, //Higher threshold for canny edge detector
			   100, //Threshold at the center detection stage
			   min_radius, //min radius
			   max_radius //max radius
			 );
			   
			 for(int i = 0; i < circles.total(); i++){
			    CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
			    CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
			    int radius = Math.round(circle.z());      
			    cvCircle(src, center, radius, new CvScalar(255, 187, 51, 255), 6, CV_AA, 0);
			    xPos=center.x();
			    yPos=center.y();
			    cirCount++;
			 }
		}else{
			System.out.println("<<<ERROR.....No image>>>");
		}
		 
		 return src;
	}

}
