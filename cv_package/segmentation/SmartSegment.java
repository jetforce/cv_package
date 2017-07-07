package cv_package.segmentation;


import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Form;

public class SmartSegment {

	//This segmentation uses eucledian distance as a basis for relevant components
	//it assumes that a perfect input exists and will use it as a basis of what is correct.
	
	public void printFormFeatures(List<MatOfPoint> contours){
		Rect temp; 
		for(int i=0; i< contours.size(); i++){
			temp = Imgproc.boundingRect(contours.get(i));
			System.out.println("Rect features x:"+temp.x +" y:"+temp.y+" width"+ temp.width+ " height "+temp.height);
		}
	}
	
	
}
