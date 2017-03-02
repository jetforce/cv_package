package segmentation;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import helpers.ComputerVision;
import helpers.Filtering;
import helpers.Sorting;

public class FourSquareCorner {

	
	Filtering filter = Filtering.getInstance();
	Sorting sort = Sorting.getInstance();
	ComputerVision vision = ComputerVision.getInstance();
	
	public Mat Normalize(Mat original){
		
		Mat paper = original.clone();
		//vision.gaussianBlur(paper);
		
		vision.grayscale(paper);
		vision.threshold(paper,true);
		ArrayList<MatOfPoint> squareConts = vision.getSquareContours(paper, 60);
		
		List<Point> points = new ArrayList<>();
		List<MatOfPoint> cornerBoxes = this.getCornerBoxes(squareConts);
		
		points.add(getBL(cornerBoxes.get(0)));
		points.add(getBR(cornerBoxes.get(1)));
		points.add(getTR(cornerBoxes.get(2)));
		points.add(getTL(cornerBoxes.get(3)));
		
		Mat normalized = getFourpointTransform(points, original);
		
		Imgcodecs.imwrite("scratch/normalized"+".png", normalized);		
		Imgproc.circle(original, points.get(0) , 10, new Scalar(0,0,0), 8);
		Imgproc.circle(original, points.get(1) , 10, new Scalar(0,0,255), 8);
		Imgproc.circle(original, points.get(2) , 10, new Scalar(0,255,0), 8);
		Imgproc.circle(original, points.get(3) , 50, new Scalar(255,0,0), 50);
		Imgproc.drawContours(original, cornerBoxes, 0, new Scalar(0,0,0),8);
		Imgproc.drawContours(original, cornerBoxes, 1, new Scalar(0,0,255),8);
		Imgproc.drawContours(original, cornerBoxes, 2, new Scalar(0,255,0),8);
		Imgproc.drawContours(original, cornerBoxes, 3, new Scalar(255,0,0),8);
		
		Imgcodecs.imwrite("scratch/TheConts"+".png", original);
		return paper;
	}
	
	
	public List<MatOfPoint> getCornerBoxes(List<MatOfPoint> squareConts){
		List<MatOfPoint> cornerSquares = new ArrayList<>();
		squareConts = sort.contourPositions(squareConts);
			
		cornerSquares.add(squareConts.get(1));
		cornerSquares.add(squareConts.get(0));
		cornerSquares.add(squareConts.get(squareConts.size()-1));
		cornerSquares.add(squareConts.get(squareConts.size()-2));
		
		return cornerSquares;
	}
	
	
	public Point getTL(MatOfPoint cont){
		Rect rect = Imgproc.boundingRect(cont);
		return rect.tl();
	}
	
	public Point getTR(MatOfPoint cont){
		Rect rect = Imgproc.boundingRect(cont);
		return new Point(rect.br().x, rect.tl().y);
	}
	
	public Point getBR(MatOfPoint cont){
		Rect rect = Imgproc.boundingRect(cont);
		return rect.br();
	}
	
	
	public Point getBL(MatOfPoint cont){
		Rect rect = Imgproc.boundingRect(cont);
		return new Point(rect.tl().x, rect.br().y);
	}
	
	
    public Mat getFourpointTransform(List<Point> points, Mat srcImage){
        //Destination matrix is computed using point list.
        Mat src = new MatOfPoint2f( points.get(0), points.get(1),points.get(2),points.get(3));

        double aWidth = computeDistance(points.get(0),points.get(1));
        double bWidth = computeDistance(points.get(2),points.get(3));
        double maxWidth;
        if(aWidth>bWidth){
            maxWidth = aWidth;
        }else {
            maxWidth =  bWidth;
        }

        double aHeight = computeDistance(points.get(0),points.get(3));
        double bHeight=  computeDistance(points.get(1),points.get(2));
        double maxHeight;

        if(aHeight>bHeight){
            maxHeight = aHeight;
        }else{
            maxHeight= bHeight;
        }

        Mat dst = new MatOfPoint2f(new Point(0, 0), new Point(maxWidth - 1, 0), new Point(maxWidth - 1, maxHeight - 1), new Point(0, maxHeight - 1));

        Mat destImage = new Mat((int) maxHeight,(int) maxWidth , srcImage.type());
        Mat transform = Imgproc.getPerspectiveTransform(src, dst);
        Imgproc.warpPerspective(srcImage, destImage, transform, destImage.size());
        return destImage;
    }
	
	
    public double computeDistance(Point p1, Point p2){
        return Math.sqrt( Math.pow(p1.x - p2.x,2)  +  Math.pow(p1.y - p2.y,2) );
    }
    
    
}
