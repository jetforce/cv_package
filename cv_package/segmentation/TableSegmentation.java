package cv_package.segmentation;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Table;
import cv_package.helpers.BorderHandler;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.MarkRecogTools;
import cv_package.helpers.Sorting;

public class TableSegmentation {

	private static Sorting sort = Sorting.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static MarkRecogTools markTools = MarkRecogTools.getInstance();
	
	private static TableSegmentation ts = new TableSegmentation();
	public static TableSegmentation getInstance(){
		return ts;
	}
	
	
	public void segment(Mat image){
		
		Imgcodecs.imwrite("WhatImWorkingOn.jpg",image);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(image, contours, new Mat(),Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		
		contours =  cv.getLargestContours(contours,2);
				
		contours = sort.contourPositions(contours);
		
		Rect temp;
		Mat m;
		
		temp = Imgproc.boundingRect(contours.get(0));
		m = image.submat(temp);
		
		segmentIndividual(m);
		
		/*
		for(int i=0; i< contours.size(); i++){
			temp = Imgproc.boundingRect(contours.get(i));
			m = image.submat(temp);
			Imgcodecs.imwrite("Bro"+i+".jpg", m);	
		}*/
			
	}
	
	
	public void go(Table tableType){
		
		
		Mat subImage = tableType.image;
		
		
		BorderHandler bh = new BorderHandler();
		Mat borders = bh.getBorders(subImage);
		
		
		Mat blank = new Mat(subImage.rows(), subImage.cols(), CvType.CV_8UC1, new Scalar(0));
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(borders, contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		//Always add one to consider the biggest square containing everything 
		//Num Rows times 2 because there are two columns.
		//For now it only supports 2 columns change in the future please
		contours = cv.getLargestContours(contours, tableType.numRows*2 +1);
		contours = sort.contourPositions(contours);
		
		Rect temp;
		Mat m;
		
		//the count starts with 1 because the first contour is the border.
		for(int i=1; i< contours.size(); i++){
			temp = Imgproc.boundingRect(contours.get(i));
			m = subImage.submat(temp);
			if(i%2==0){
				//Blob
				tableType.addBlob(m);
				
			}else{
				//OMR
				tableType.addMark(m, markTools.countWhites(m));	
			}
		}
		
		
	}
	
	
	
	
	
	// This functions is no longer needed
	public void segmentIndividual(Mat subImage){
		
		BorderHandler bh = new BorderHandler();
		Mat borders = bh.getBorders(subImage);
		
		Imgcodecs.imwrite("Hellllloo.jpg",borders);
		
		Mat blank = new Mat(subImage.rows(), subImage.cols(), CvType.CV_8UC1, new Scalar(0));
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(borders, contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		//Always add one to consider the biggest square containing everything
		contours = cv.getLargestContours(contours, 26+1);
		contours = sort.contourPositions(contours);
		
		
		ArrayList<Mat> blobs = new ArrayList<>();
		ArrayList<Mat> omrs = new ArrayList<>();
		Rect temp;
		Mat m;
		
		
		for(int i=1; i< contours.size(); i++){
			temp = Imgproc.boundingRect(contours.get(i));
			m = subImage.submat(temp);
			if(i%2==0){
				//Blob
				blobs.add(m);
				Imgcodecs.imwrite("blob"+i+".jpg",m);
				
			}else{
				//OMR
				omrs.add(m);	
				Imgcodecs.imwrite("omr"+i+".jpg",m);
			}
		}
		
		Imgproc.drawContours(blank, contours, -1, new Scalar(255),1);
		Imgcodecs.imwrite("topConts.jpg", blank);
		
	}
	
	

	
	
	
	
	
}
