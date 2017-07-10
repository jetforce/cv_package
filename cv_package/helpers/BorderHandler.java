package cv_package.helpers;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class BorderHandler {
	
	
	private static Sorting sort = Sorting.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
    private static BorderHandler bh = new BorderHandler();
    public static BorderHandler getInstance() { return bh; }
	
	
	public Mat getBorders(Mat paper, int width, int height){	
		Mat filled = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		Mat filled2 = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		Mat result = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		
		Imgproc.morphologyEx(paper, filled, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(paper.cols()/width,1)));
		Imgproc.morphologyEx(paper, filled2, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(1,paper.rows()/height)));
		
		Core.add(filled, filled2, result);
		//Imgproc.morphologyEx(m, m, Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(m.cols(),1),new Point(-1,-1)));
		//Imgproc.morphologyEx(m, m, Imgproc.MORPH_DILATE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(m.cols(),1), new Point(-1,-1)));
		return result;
	}
    
    
	public Mat removeBorder4(Mat image) {
		// made for OMR 
		
		Mat borders = getBorders(image, 3,3);
		Mat nomore = removeBorder(image, borders);
		
	
		return nomore;
	}
	
	public Mat removeBorder(Mat m, Mat location){
		
		Mat filled = new Mat(m.rows(), m.cols(), CvType.CV_8UC1, new Scalar(255));
		Mat inv = location;
		cv.invert(inv);
		List<MatOfPoint> contours = cv.findContours(inv, Imgproc.RETR_TREE);
		contours = this.getLargestContours(contours, 1);
		MatOfPoint outline = this.getApprox(contours.get(0));
		contours.add(outline);
		//saver.saveImage("scratch",num+"filled", filled);
		Imgproc.drawContours(filled,contours , -1,new Scalar(0),1);
		
		Imgcodecs.imwrite("scratch.jpg", filled);
		
		//saver.saveImage("scratch",num+"filledconts", filled);
		
		Rect rect = Imgproc.boundingRect(outline);
		int midX = rect.width/2;
		int midY = rect.height/2;
        Imgproc.floodFill(filled, new Mat(), new Point(midX, midY), new Scalar(0));
        //saver.saveImage("scratch", num+"Afterfilled", filled);
        cv.invert(m);
        Mat result = new Mat(m.rows(), m.cols(), CvType.CV_8UC1);
        //saver.saveImage("scratch", num+"orig",m);
		Core.add(m, filled,result);
		cv.invert(result);
		return result;
		//saver.saveImage("scratch", num+"result",result);
				
	}
	 
	
	
	
	
    
    
    
    
	public Mat getBorders(Mat paper){
		
		Mat filled = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		Mat filled2 = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		Mat result = new Mat(paper.rows(), paper.cols(), CvType.CV_8UC1, new Scalar(0));
		
		Imgproc.morphologyEx(paper, filled, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(paper.cols()/30,3)));
		Imgproc.morphologyEx(paper, filled2, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3,paper.rows()/30)));
		
		Core.add(filled, filled2, result);
		//Imgproc.morphologyEx(m, m, Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(m.cols(),1),new Point(-1,-1)));
		//Imgproc.morphologyEx(m, m, Imgproc.MORPH_DILATE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(m.cols(),1), new Point(-1,-1)));
		return result;
	}
	
	
		
	public Mat removeBorder(Mat m){
		
		Mat filled = new Mat(m.rows(), m.cols(), CvType.CV_8UC1, new Scalar(255));
		Mat inv = m.clone();
		cv.invert(inv);
		List<MatOfPoint> contours = cv.findContours(inv, Imgproc.RETR_TREE);
		contours = this.getLargestContours(contours, 1);
		MatOfPoint outline = this.getApprox(contours.get(0));
		contours.add(outline);
		//saver.saveImage("scratch",num+"filled", filled);
		Imgproc.drawContours(filled,contours , -1,new Scalar(0),1);
		
		Imgcodecs.imwrite("scratch.jpg", filled);
		
		//saver.saveImage("scratch",num+"filledconts", filled);
		
		Rect rect = Imgproc.boundingRect(outline);
		int midX = rect.width/2;
		int midY = rect.height/2;
        Imgproc.floodFill(filled, new Mat(), new Point(midX, midY), new Scalar(0));
        //saver.saveImage("scratch", num+"Afterfilled", filled);
        cv.invert(m);
        Mat result = new Mat(m.rows(), m.cols(), CvType.CV_8UC1);
        //saver.saveImage("scratch", num+"orig",m);
		Core.add(m, filled,result);
		cv.invert(result);
		return result;
		//saver.saveImage("scratch", num+"result",result);
				
	}
	 
	public Mat shrinkPicture(Mat mat, double percent){
		Rect rect = new Rect(0,0,mat.cols(),mat.rows());
		Mat img = mat.submat(rect);
		int heightAdjust =  (int) (mat.rows() * percent);
		int widthAdjust = 	(int) (mat.cols() * percent);
		img.adjustROI(-heightAdjust, -heightAdjust, -widthAdjust, -widthAdjust);
		return img;
	}
	
	
	
	
	
	public List<MatOfPoint> getLargestContours(List<MatOfPoint> contours, int elementCount) {
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		return contours.subList(0, elementCount);
	}

    public MatOfPoint getApprox(MatOfPoint thisContour){
        MatOfPoint2f thisContour2f = new MatOfPoint2f();
        MatOfPoint approxContour = new MatOfPoint();

        MatOfPoint2f approxContour2f = new MatOfPoint2f();
        thisContour.convertTo(thisContour2f, CvType.CV_32FC2);

        double perimeter = Imgproc.arcLength(thisContour2f,true);

        Imgproc.approxPolyDP(thisContour2f, approxContour2f, perimeter*0.04, true);
        approxContour2f.convertTo(approxContour, CvType.CV_32S);

        return approxContour;

    }
    
    
	
	
}
