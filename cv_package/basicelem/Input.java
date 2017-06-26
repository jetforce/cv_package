package cv_package.basicelem;

import java.util.ArrayList;

import org.opencv.core.Mat;

import cv_package.debug.LocalSaver;
import cv_package.segmentation.OpticalMarkRecognition;

public class Input {

	public String name;
	public int count;
	public ArrayList<Mat> images;
	
	public Input(String name, int count) {
		this.name = name;
		this.count = count;
		images = new ArrayList<>();
	}
	
	public void addImage(Mat newImg) {
		images.add(newImg);
	}
	
	public String getString() {
		return "";
	}
	
	public char getLetter(int index) {
		return 0;
	}
	
	public boolean getMark() {
		if(images.size() != 0) {
//			OpticalMarkRecognition omr = new OpticalMarkRecognition(fileSaver);
//			omr.recognize(images.get(0), images.size()-1, i)
			return true;
		}
		else
			return false;
	}
	
	public Mat getThresh() {
		if(images.size() != 0)
			return images.get(0);
		else
			return null;
	}
	
}
