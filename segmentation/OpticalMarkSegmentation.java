package segmentation;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import helpers.ComputerVision;
import helpers.Filtering;
import helpers.HierarchyHandler;
import helpers.Sorting;

public class OpticalMarkSegmentation {

	
	public static ComputerVision cv = ComputerVision.getInstance();
	public static OpticalMarkSegmentation markSegmenter = new OpticalMarkSegmentation();
	public static OpticalMarkSegmentation getInstance(){return markSegmenter;}
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static HierarchyHandler hierarchyHandler =  HierarchyHandler.getInstance();
	
	
	public void recognize(Mat box,int i){
		
		//Imgcodecs.imwrite("scratch/test_boxes"+i+".png", box);

		List<MatOfPoint> contours = new ArrayList<>();
		
		
		Mat hierarchy = new Mat();
		
		
		Imgproc.findContours(box, contours, hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
		//System.out.println("contour size"+ contours.size());
		//System.out.println(i+ ") This is the hierarchy "+ hierarchy.dump()+ " Width "+ hierarchy.width()+" Height "+hierarchy.height());
		//System.out.println("Value "+ hierarchy.get(0, 0)[0]+" , "+ hierarchy.get(0, 0)[1]+ " , "+ hierarchy.get(0, 0)[2]);
		
		//List<MatOfPoint> sortedContour = sort.contourAreas(contours, false);
		//Rect rect = Imgproc.boundingRect(sortedContour.get(0));
    	//Mat innerBox = box.submat(rect);
    	
    	int largestContour = hierarchyHandler.getLargestBoundingBox(contours);
    	
    	ArrayList<Integer> children = hierarchyHandler.getChildren(largestContour, hierarchy);
    	
    	if(children.size() == 1 && hierarchy.get(0, children.get(0))[2] != -1){
    		children = hierarchyHandler.getChildren(children.get(0), hierarchy);
    	}
    	
    	
    	List<MatOfPoint> circles = new ArrayList<>();
    	
    	System.out.println("Number of children "+ children.size());
    	Rect rect;
    	for(int x: children){
    		//rect = Imgproc.boundingRect(contours.get(x));
    		//Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
    		circles.add( contours.get(x));
    	}
    	
    	
    	
    	circles = sort.contourPositions(circles);
    	Mat cho;
    	Mat roi;
    	///Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
    	for(int x=0; x<circles.size();x++){
    		rect = Imgproc.boundingRect(circles.get(x));
    		roi = new Mat(box,rect);
    		System.out.println("Choice "+x+" ,"+countWhite(roi));
    		//Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
    		//Imgproc.putText(box, "Number "+x , rect.tl(), Core.FONT_HERSHEY_PLAIN, (double)20.0 , new Scalar(0,255,255));
    	
    	}
    	
    	
    	//Imgproc.drawContours(box,contours, largestContour,new Scalar(255,255,255),-1);
    	Imgcodecs.imwrite("scratch/Final"+i+".png", box);
    	//Imgcodecs.imwrite("scratch/inner_boxes"+i+".png", innerBox);
		
	}
	
	
	public int countWhite(Mat box){
		
		int count=0;
		
		for(int i=0; i< box.width(); i++){
			for(int j=0;j<box.height(); j++){
				if((int) box.get(j, i)[0] == 255){
					count++;
				}
			}
		}
		
		
		return count;
		
	}
	
	
	
	
}
