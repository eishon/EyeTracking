import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.JFrame;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class Main {
	
	private static int x=0,y=0;
	public static int rect_x=0,rect_y=0,rect_length=50;
	public static int screen_width=0,screen_height=0;
	
	public static Thread left_click_thread;
	public static boolean leftClick=false,leftClickChk = false;
	public static int sensitivity_left_click=20;
	
	public static CvMemStorage memory,faceMemory;
	public static Controller controller;
	public static ImageProcessing imageProcessing;
	public static HoughTransform imgProcs;
	public static ColorFilter filter;
	public static Gray2BinImage gray2BinImage;

	public static void main(String[] args) throws AWTException {
	
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screen_width = (int) screenSize.getWidth();
		screen_height = (int) screenSize.getHeight();
		System.out.println("Screen Size: "+screen_width+" x "+screen_height);
		
		memory = CvMemStorage.create();
	    faceMemory=CvMemStorage.create();
	    controller=new Controller(640,480);
		imageProcessing=new ImageProcessing();
		imgProcs=new HoughTransform(memory);
		filter=new ColorFilter();
		gray2BinImage=new Gray2BinImage();
		
		CvHaarClassifierCascade face_default = new CvHaarClassifierCascade(cvLoad("resources/haarcascade_frontalface_default.xml"));
		CvHaarClassifierCascade eye_default = new CvHaarClassifierCascade(cvLoad("resources/eye.xml"));
		
		Robot mouse=new Robot();
		
		CvRect rect=new CvRect(325, 213, 140, 130);
		
		CanvasFrame eye_frame_1 = new CanvasFrame("Eye 1");
		CanvasFrame eye_frame_2 = new CanvasFrame("Eye 2");
		CanvasFrame eye_frame_3 = new CanvasFrame("Eye 3");
		CanvasFrame original = new CanvasFrame("Original");
		
		original.setLocation(10,10);
		eye_frame_1.setLocation(700,10);
		eye_frame_2.setLocation(10,300);
		eye_frame_3.setLocation(350,350);
		
		controller.setSize(720,450);
		controller.setLocation(640,300);
		controller.setVisible(true);
		
		original.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eye_frame_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eye_frame_2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eye_frame_3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CvSeq contour1 = new CvSeq(), contour2;
		CvMemStorage storage = CvMemStorage.create();
		CvMoments moments = new CvMoments(Loader.sizeof(CvMoments.class));
				
		double areaMax, areaC=0;
		double m10,m01,m_area;
		
		FrameGrabber grabber = new OpenCVFrameGrabber("");
		
		//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("images/8.mp4");
		      
		try {
			grabber.start();
			
			int height=grabber.getImageHeight();
			int width=grabber.getImageWidth();
			System.out.println("Screen Size: "+width+" x "+height);
			
			IplImage img=null,eye=null,gray=null,gray_h=null,bin_img=null,hough=null;
			
			while (true) {
				
				img = grabber.grab();
				
				original.setCanvasSize(width,height);
				eye_frame_1.setCanvasSize(400,400);
				eye_frame_2.setCanvasSize(400,400);
				eye_frame_3.setCanvasSize(400,400);
		        
				if (img != null) {
					
					rect=new CvRect(rect_x, rect_y, rect_length, rect_length);
					
					img=imageProcessing.flip_img(img);
					//img=filter.color_histrogram(img);
					
					cvRectangle (img,cvPoint(rect.x(), rect.y()),
							cvPoint(rect.width() + rect.x(), rect.height() + rect.y()),
							CvScalar.BLUE,
							2,
							CV_AA,
							0);
					
					
					//eye=detect(img, faceMemory, eye_default);
					eye=section_img(img, rect);
					
					if (eye!=null) {
						gray = IplImage.create(cvGetSize(eye), IPL_DEPTH_8U, 1);
						gray_h = IplImage.create(cvGetSize(eye), IPL_DEPTH_8U, 1);
						bin_img = IplImage.create(cvGetSize(eye), IPL_DEPTH_8U, 3);
						hough = IplImage.create(cvGetSize(eye), IPL_DEPTH_8U, 3);
						
						cvCvtColor(eye, gray, CV_BGR2GRAY);
						cvEqualizeHist(gray, gray_h);
						
						bin_img=gray2BinImage.convert_gray2bin(gray_h);
						//bin_img=filter.color_filter(eye);

					}
					
					original.showImage(img);
					if (eye!=null) {
						
						find_position();
						
						contour1 = new CvSeq();
						areaMax= 10;
					
						cvFindContours(bin_img,storage,contour1,Loader.sizeof(CvContour.class),
										CV_RETR_LIST,CV_LINK_RUNS,cvPoint(0,0));
					
						contour2= contour1;
					
						while(contour1 != null && !contour1.isNull())
						{
							areaC = cvContourArea(contour1,CV_WHOLE_SEQ,1);
						
							if(areaC>areaMax)
								areaMax = areaC;
						
							contour1 = contour1.h_next();
						
						}
					
						while(contour2 !=null && !contour2.isNull())
						{
							areaC= cvContourArea(contour2,CV_WHOLE_SEQ,1);
						
							if(areaC<areaMax)
							{
								cvDrawContours(bin_img,contour2,CV_RGB(0,0,0),CV_RGB(0,0,0),
										0,CV_FILLED,8,cvPoint(0,0));
							}
						
							contour2=contour2.h_next();
						}
					
						cvMoments(bin_img,moments,1);
						
						m10 = cvGetSpatialMoment(moments,1,0);
						m01 = cvGetSpatialMoment(moments,0,1);
						m_area = cvGetCentralMoment(moments,0,0);
						
						x = (int) (m10/m_area);
						y = (int) (m01/m_area);
						
						if (leftClickChk!=true) {
							left_click(x,y);
						}
						
						cvCircle(eye, cvPoint(x,y), 2, cvScalar(0,0,255,0), 2,0,0);
						if (leftClick) {
							cvCircle(eye, cvPoint(x,y), 6, cvScalar(0,255,0,0), 2,0,0);
							leftClick=false;
						}
						
						x=x-67;
						y=y-65;
						
						if(x>0 && y>0)
							System.out.println("x = "+x+", y= "+y);
						
						
						eye_frame_1.showImage(eye);
						eye_frame_2.showImage(gray_h);
						eye_frame_3.showImage(bin_img);
						
					}
					
					
					
					if(x > 0 && y > 0) 
					{
						
						//mouse.mouseMove(x*54, y*45);
						
					}
					
				}
			}	
		}
		catch (Exception e) {
			
		}
		
		cvClearMemStorage(memory);
		cvClearMemStorage(faceMemory);
		
		System.out.println(imgProcs.cirCount);
		
	}
	
	public static IplImage detect(IplImage src,CvMemStorage memStorage,CvHaarClassifierCascade cascade){
		IplImage image = null;
		
		CvSeq sign = cvHaarDetectObjects(
				src,
				cascade,
				memStorage,
				1.7,
				3,
				CV_HAAR_DO_CANNY_PRUNING);
		
		int total_objects = sign.total();
		
		CvRect r=null;
		
		for(int i = 0; i < total_objects; i++){
			r = new CvRect(cvGetSeqElem(sign, i));
			System.out.println(r);
			cvRectangle (
					src,
					cvPoint(r.x(), r.y()),
					cvPoint(r.width() + r.x(), r.height() + r.y()),
					CvScalar.RED,
					2,
					CV_AA,
					0);
				}
		
		if (r!=null) {
			image = IplImage.create(r.width(), r.height(), src.depth(), src.nChannels());
			cvSetImageROI(src, r);
			cvCopy(src, image);
			cvResetImageROI(src);
		}
		System.out.println(total_objects);
		
		return image;
		
	}
	
	public static IplImage[] detectEye(IplImage src,CvMemStorage memStorage,CvHaarClassifierCascade cascade){
		IplImage eye_image[]=new IplImage[10];
	
		CvSeq sign = cvHaarDetectObjects(
				src,
				cascade,
				memStorage,
				1.2,
				2,
				CV_HAAR_DO_CANNY_PRUNING);
		
		
		int total_eyes = sign.total();
		
		CvRect r=null;
		
		for(int i = 0; i < total_eyes; i++){
			r = new CvRect(cvGetSeqElem(sign, i));
			System.out.println(r);
			
			cvRectangle (
					src,
					cvPoint(r.x(), r.y()),
					cvPoint(r.width() + r.x(), r.height() + r.y()),
					CvScalar.RED,
					2,
					CV_AA,
					0);
			
			if (r!=null) {
				eye_image[i] = IplImage.create(r.width(), r.height(), src.depth(), src.nChannels());
				cvSetImageROI(src, r);
				cvCopy(src, eye_image[i]);
				cvResetImageROI(src);
			}
			
		}
		
		System.out.println(total_eyes);
		
		return eye_image;
		
	}
	
	public static IplImage section_img(IplImage src, CvRect r){
		
		IplImage image = IplImage.create(r.width(), r.height(), src.depth(), src.nChannels());
		cvSetImageROI(src, r);
		cvCopy(src, image);
		cvResetImageROI(src);
		
		return image;
		
	}
	
	public static void find_position(){
		
	}
	
	public static void left_click(int x_left,int y_left){
        leftClickChk = false;
        
        left_click_thread = new Thread(new Runnable() {
            public void run() {
            	int i=0;
            	int x_left_click=x_left;
            	int y_left_click=y_left;
                //while (!Thread.currentThread().isInterrupted() && !stopWorker) {
            	System.out.println("Init Click Seq");
                while (i<7) {
                    try {
                        Thread.sleep(100);
                        i++;
                        
                        if (((x_left_click+sensitivity_left_click)>x)&&((x_left_click-sensitivity_left_click)<x)) {
							if (((y_left_click+sensitivity_left_click)>y)&&((y_left_click-sensitivity_left_click)<y)) {
								leftClick = true;
							}
						}
                    	
                    } catch (Exception ex) {
                        leftClickChk = true;
                        System.out.println("Interrupt");
                    }
                }
            }
        });

        left_click_thread.start();
		
	}
	
}