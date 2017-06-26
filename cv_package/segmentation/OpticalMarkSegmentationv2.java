package cv_package.segmentation;



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

import cv_package.debug.LocalSaver;
import cv_package.helpers.BorderHandler;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.HierarchyHandler;
import cv_package.helpers.Sorting;

public class OpticalMarkSegmentationv2 {



	
	public static ComputerVision cv = ComputerVision.getInstance();
	//public static OpticalMarkSegmentation markSegmenter = new OpticalMarkSegmentation();
	private LocalSaver saver;


	public OpticalMarkSegmentationv2(LocalSaver saver) {
		this.saver = saver;
	}
	
	//public static OpticalMarkSegmentation getInstance(){return markSegmenter;}
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static HierarchyHandler hierarchyHandler =  HierarchyHandler.getInstance();
	private static BorderHandler bh = BorderHandler.getInstance();
	
	
	
	public int[] recognize(Mat box,int numChoices,int i){

		saver.saveImage("Box" + i, box);

		Mat individual = box.clone();

		
		int answerValues[] = new int[numChoices];


		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(box, contours, hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
		

		int largestContour = hierarchyHandler.getLargestBoundingBox(contours);
		System.out.println("This is the largest contour "+ largestContour+" Size "+ Imgproc.contourArea(contours.get(largestContour)));
		ArrayList<Integer> children = hierarchyHandler.getChildren(largestContour, hierarchy);
		int largestChild = getLargestChild(contours,children);
		double thresh = Imgproc.contourArea(contours.get(largestContour)) / numChoices;
		
		
		Rect boundingRectOfChoices =  Imgproc.boundingRect(contours.get(largestChild));
		
		return getAnswers(box.submat(boundingRectOfChoices),numChoices);
		
		
	}


	
	
	public int[] getAnswers(Mat image, int numChoices){
		//Input image is the Box with the actual choices.
		//It is assumed the image has border
		int answerValues[] = new int[numChoices];
		Mat borderless = bh.removeBorder(image);
		Rect temp;
		int numshade;
		
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(borderless, contours, hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
		
		contours = filterGroups(contours,numChoices);
		
		for(int i=0;i< numChoices;i++){
			temp = Imgproc.boundingRect(contours.get(i));
			numshade = this.countWhite(borderless.submat(temp));
			Imgcodecs.imwrite("wat"+numChoices+"_"+i +".jpg",borderless.submat(temp));
			answerValues[i] = numshade;
		}
		
		
		
		
		Imgproc.drawContours(borderless, contours,-1, new Scalar(255),-1);
		
		//Imgcodecs.imwrite("noBorder.jpg", borderless);
		return answerValues;
	}
	
	

	
	public List<MatOfPoint> filterGroups(List<MatOfPoint> contours, int elementCount) {
		List<MatOfPoint> contours2 = new ArrayList<>(contours);
		contours2 = filter.getLargestN(contours2, elementCount);
		contours2 = sort.contourPositions(contours2);
		return contours2;
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

