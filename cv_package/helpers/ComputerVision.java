package cv_package.helpers;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hannah on 1/27/2017.
 */
public class ComputerVision {

    private static ComputerVision cv = new ComputerVision();
    public static ComputerVision getInstance() { return cv; }
    public static Sorting sort = Sorting.getInstance();
    
    private ComputerVision() { }

    public void grayscale(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
    }


    public void threshold(Mat image, boolean isInverted,int window) {
        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, window, 6);
        if(isInverted)
            Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
    }


    public void threshold(Mat image, boolean isInverted) {
        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 305, 6);
        if(isInverted)
            Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
    }
    
    public void invert(Mat image) {
        Imgproc.threshold(image, image, 0, 255, Imgproc.THRESH_BINARY_INV);
    }

    public void morph(Mat fromMat, int op, int shape, int size) {
        Imgproc.morphologyEx(fromMat, fromMat, op, Imgproc.getStructuringElement(shape, new Size(size,size)));
    }
    
    public void preprocess(Mat mat) {
        grayscale(mat);
        threshold(mat, true);
    }
    
    public void preprocess2(Mat mat) {
        grayscale(mat);
        threshold(mat, false);
    }
    
    public ArrayList<MatOfPoint> findContours(Mat img, int heirarchy) {
        ArrayList<MatOfPoint> contoursFound = new ArrayList<>();
        //Imgproc.findContours(img, contoursFound, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(img, contoursFound, new Mat(), heirarchy, Imgproc.CHAIN_APPROX_NONE);
//        Imgproc.findContours(img, contoursFound, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.reverse(contoursFound);
        return contoursFound;
    }
    
    public ArrayList<MatOfPoint> findContoursExt(Mat img) {
        ArrayList<MatOfPoint> contoursFound = new ArrayList<>();
        Imgproc.findContours(img.clone(), contoursFound, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Collections.reverse(contoursFound);
        return contoursFound;
    }
    
    public void gaussianBlur(Mat image){
    	 Imgproc.GaussianBlur(image, image,new Size(45,45), 0);
    }
    
    public ArrayList<MatOfPoint> getSquareContours(Mat image, int size, int hierchy){
        MatOfPoint temp;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), hierchy,Imgproc.CHAIN_APPROX_SIMPLE);
        ArrayList<MatOfPoint> squares = new ArrayList<>();
        
       
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > size){
                    temp = this.getSquareApprox(contours.get(i));
                    if(temp!=null){
                        squares.add(temp);
                    }
            }
        }
        return squares;
    }
    
    public ArrayList<MatOfPoint> getSquareContours(Mat image, int size){
        MatOfPoint temp;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        ArrayList<MatOfPoint> squares = new ArrayList<>();
        
       
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > size){
                    temp = this.getSquareApprox(contours.get(i));
                    if(temp!=null){
                        squares.add(temp);
                    }
            }
        }
        return squares;
    }
    
    public ArrayList<MatOfPoint> getApproxContours(List<MatOfPoint> contours){
    	
        MatOfPoint temp;
        ArrayList<MatOfPoint> squares = new ArrayList<>();
         
        for(int i=0; i< contours.size();i++){
        	temp = this.getApprox(contours.get(i));
        	squares.add(temp);
        }
        return squares;
    }
    
    
    public ArrayList<MatOfPoint> getApproxContours(int hiarchy, Mat image){
    	
        MatOfPoint temp;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), hiarchy,Imgproc.CHAIN_APPROX_SIMPLE);
        ArrayList<MatOfPoint> squares = new ArrayList<>();
               
        for(int i=0; i< contours.size();i++){
        	temp = this.getApprox(contours.get(i));
        	squares.add(temp);
        }
        return squares;
    }
    
    public ArrayList<MatOfPoint> getApproxContours(Mat image, int size){
    	
        MatOfPoint temp;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        ArrayList<MatOfPoint> squares = new ArrayList<>();
               
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > size){
                    temp = this.getApprox(contours.get(i));
                    squares.add(temp);
            }
        }
        return squares;
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
    
    
    
    public MatOfPoint getSquareApprox(MatOfPoint thisContour){
        MatOfPoint2f thisContour2f = new MatOfPoint2f();
        MatOfPoint approxContour = new MatOfPoint();
        MatOfPoint2f approxContour2f = new MatOfPoint2f();
        thisContour.convertTo(thisContour2f, CvType.CV_32FC2);
        double perimeter = Imgproc.arcLength(thisContour2f,true);
        Imgproc.approxPolyDP(thisContour2f, approxContour2f, perimeter*0.04, true);
        approxContour2f.convertTo(approxContour, CvType.CV_32S);

        if (approxContour.size().height == 4) {
            return approxContour;
        }
        return null;
    }
    
    
	public List<MatOfPoint> getLargestContours(List<MatOfPoint> contours, int elementCount) {
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		return contours.subList(0, elementCount);
	}

	
    
    
    
}
