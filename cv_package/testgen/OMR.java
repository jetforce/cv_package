package cv_package.testgen;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Mark;
import cv_package.debug.LocalSaver;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.HierarchyHandler;
import cv_package.helpers.Sorting;

public class OMR {

	public static ComputerVision cv = ComputerVision.getInstance();
	private LocalSaver saver;

	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static HierarchyHandler hierarchyHandler =  HierarchyHandler.getInstance();
	
	private static double THRESH_PERCENT = 0.85;
	
	private static OMR omr = new OMR();
    public static OMR getInstance() { return omr; }
    private OMR() { }
	
	public void go(Mark comp) {
		List<Mat> mats = getMatCirclesClean(comp.image, comp.labels.size()+1);
		List<Integer> values = countWhiteOfMats(mats);
		List<Boolean> decisions = decideIfMarked(values);
		comp.markMats = new ArrayList<>(mats);
		comp.markValues = new ArrayList<>(values);
		comp.markDecision = new ArrayList<>(decisions);
	}
	
	public boolean isMarked(int value, int basePixelWhite) {
		if(value >= basePixelWhite*THRESH_PERCENT)
			return true;
		else
			return false;
	}
	
	public boolean isMarked(Mat image, int basePixelWhite) {
		if(countWhite(image) >= basePixelWhite*THRESH_PERCENT)
			return true;
		else
			return false;
	}
	
	public List<Mat> getMatCircles(Mat image, int indices) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
//		System.out.println("conts-"+contours.size());
//		filter.draw(image, contours, 5);
		return filter.largeAreaElements(image, contours, indices);
	}
	
	public List<Mat> getMatCirclesClean(Mat image, int indices) {
		image = filter.removeOutline2(image); 			
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
		return filter.largeAreaElements(image, contours, indices);
	}
	
	public List<Boolean> decideIfMarked(List<Integer> values) {
		ArrayList<Boolean> decisions = new ArrayList<>();
		int basePixelWhite = values.get(0);
		for(int i = 1; i < values.size(); i++) {
			decisions.add(isMarked(values.get(i), basePixelWhite));
		}
		return decisions;
	}
	
	public List<Integer> countWhiteOfMats(List<Mat> mats) {
		ArrayList<Integer> values = new ArrayList<>();
		for(Mat m:mats) {
			values.add(countWhite(m));
		}
		return values;
	}

	public int countWhite(Mat box){
		
		int count = 0;
		
		for(int i = 0; i < box.width(); i++) {
			for(int j = 0; j<box.height(); j++) {
				if((int) box.get(j, i)[0] == 255) {	// 255 is white
					count++;
				}
			}
		}
		
		return count;
		
	}
}
