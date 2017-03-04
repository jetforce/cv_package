package cv_package.segmentation;

import android.util.Log;

import com.virtusio.sibayan.image_process.helpers.ImageSaver;
import com.virtusio.sibayan.thesis.activities.HomeActivity;

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

import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.HierarchyHandler;
import cv_package.helpers.Sorting;

public class OpticalMarkSegmentation extends ImageSaver {



	
	public static ComputerVision cv = ComputerVision.getInstance();
	//public static OpticalMarkSegmentation markSegmenter = new OpticalMarkSegmentation();



	public OpticalMarkSegmentation(File directory) {
		super(directory);
	}

	//public static OpticalMarkSegmentation getInstance(){return markSegmenter;}
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static HierarchyHandler hierarchyHandler =  HierarchyHandler.getInstance();
	
	
	public int[] recognize(Mat box,int numChoices,int i){


		saveImage("Box"+i, box);
		/*
		int answerValues[] = new int[numChoices];

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
			Log.d(HomeActivity.TAG,"SIZE box number "+boxNum+": circle num "+x+" "+countWhite(roi));
			//answerValues[x] = countWhite(roi);
    		//System.out.println("Choice "+x+" ,"+countWhite(roi));
    		//Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
    		//Imgproc.putText(box, "Number "+x , rect.tl(), Core.FONT_HERSHEY_PLAIN, (double)20.0 , new Scalar(0,255,255));
    	
    	}
    	
    	
    	//Imgproc.drawContours(box,contours, largestContour,new Scalar(255,255,255),-1);
    	//Imgcodecs.imwrite("scratch/Final"+i+".png", box);
    	//Imgcodecs.imwrite("scratch/inner_boxes"+i+".png", innerBox);
		return answerValues;
		*/
		Mat individual = box.clone();


		int answerValues[] = new int[numChoices];


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
		System.out.println("This is the largest contour "+ largestContour+" Size "+ Imgproc.contourArea(contours.get(largestContour)));
		ArrayList<Integer> children = hierarchyHandler.getChildren(largestContour, hierarchy);
		int largestChild = getLargestChild(contours,children);
		double thresh = Imgproc.contourArea(contours.get(largestContour)) / numChoices;
		System.out.println("Thresh is  : "+ thresh+" Largest Child size "+Imgproc.contourArea(contours.get(largestChild)));

		if(thresh < Imgproc.contourArea(contours.get(largestChild))){
			children =  hierarchyHandler.getChildren(largestChild, hierarchy);
		}

		List<MatOfPoint> sortedChildrenContours = getTopChildren(contours,children);


		List<MatOfPoint> circles = new ArrayList<>();
		System.out.println("Number of children "+ sortedChildrenContours.size());
		Rect rect;

		for(int x=0; x < numChoices; x++ ){
			//System.out.println("Children are: "+ x +  "size "+ Imgproc.contourArea(sortedChildrenContours.get(x)));
			rect = Imgproc.boundingRect(sortedChildrenContours.get(x));
			Imgproc.rectangle(individual, rect.tl(), rect.br(), new Scalar(255,255,255),20);
			circles.add( sortedChildrenContours.get(x));
		}


		circles = sort.contourPositions(circles);
		Mat cho;
		Mat roi;
		int value;
		///Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
		for(int x=0; x<circles.size();x++){
			rect = Imgproc.boundingRect(circles.get(x));
			roi = new Mat(box,rect);
			value = countWhite(roi);
			answerValues[x] = value;
			Log.d(HomeActivity.TAG,"SIZE box num "+i+ " " +countWhite(roi));
			//answerValues[x] = countWhite(roi);
			//System.out.println("Choice "+x+" ,"+countWhite(roi));
			//Imgproc.rectangle(box, rect.tl(), rect.br(), new Scalar(255,255,255),-1);
		}


		//Imgproc.drawContours(box,contours, largestContour,new Scalar(255,255,255),-1);
		//Imgcodecs.imwrite("scratch/Final"+i+".png", box);
		//Imgcodecs.imwrite("scratch/individual"+i+".png",individual);

		//Imgcodecs.imwrite("scratch/inner_boxes"+i+".png", innerBox);
		return answerValues;

	}




	public List<MatOfPoint> getTopChildren(List<MatOfPoint> conts,  ArrayList<Integer> children){
		ArrayList<MatOfPoint> sorted =  new ArrayList<>();
		for(int x:children){
			sorted.add(conts.get(x));
		}
		return sort.contourAreas(sorted, false);
	}

	public int getLargestChild(List<MatOfPoint> conts, ArrayList<Integer> children){
		int largest = children.get(0);
		int max = children.size();
		for(int i=1; i < max; i++){
			if(Imgproc.contourArea(conts.get(largest)) < Imgproc.contourArea(conts.get(children.get(i)))){
				largest = children.get(i) ;
			}
		}
		return largest;
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
