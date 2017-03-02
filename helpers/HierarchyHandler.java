package helpers;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class HierarchyHandler {

	
	public static HierarchyHandler handler = getInstance(); 
	
	public static HierarchyHandler getInstance(){ return new HierarchyHandler(); }
	
	
	
	public ArrayList<Integer> getChildren(int parent, Mat hierarchy){
		System.out.println("Parent "+parent);
		ArrayList<Integer> childrenList = new ArrayList<>();
	
		for(int i= 0; i<hierarchy.width();i++){
			//System.out.println("Contour : "+i +", " + hierarchy.get(0,i)[0] +", "+hierarchy.get(0,i)[1]+" "+hierarchy.get(0,i)[2]+" "+hierarchy.get(0,i)[3]);	
			if( (int) hierarchy.get(0, i)[3] == parent){
				childrenList.add(i);
			}
		}
		
		return childrenList;
	}
	
	
	public int getLargestBoundingBox(List<MatOfPoint> contours){
		
		double largestArea=0;
		int largestAreaIndex=0;
		Rect rect;
		double tempArea;
		
		for(int i=0; i< contours.size(); i++){
    		rect = Imgproc.boundingRect(contours.get(i));
    		tempArea = rect.area();
        	if(largestArea < tempArea){
        		largestAreaIndex= i;
        		largestArea = rect.area();
        	}
			
		}
		
		
		return largestAreaIndex;
	}
	
	
	
	
	
}
