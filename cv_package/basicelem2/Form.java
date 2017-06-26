package cv_package.basicelem2;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.testgen.OCR;
import cv_package.testgen.OMR;
import cv_package.testui.PanelBuilder;

public class Form {

	public ArrayList<Type> components;
	public int count;
	public Mat image;
	public ArrayList<Mat> processedImages = new ArrayList<>();
	
	public void process() {
		if(components.size() == 0)
			System.out.println("Error no components");
		else
			go();
	}

	OCR ocr = OCR.getInstance();
	OMR omr = OMR.getInstance();
	ComputerVision cv = ComputerVision.getInstance();
	Filtering filter = Filtering.getInstance();
	
	private void go() {
		
		List<Mat> mats = filter.largeAreaElements
				(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), components.size());
		
		int index = 0;
		
		for(Type c:components) {
			
			processedImages.add(mats.get(index));
			c.image = mats.get(index);
	    	Imgcodecs.imwrite("C:/Users/Hannah/Desktop/ex/____here"+index+".png", c.image);
			
			switch(c.typename) {
			case "TEXT": ocr.go((Text)c); break;
			case "MARK": omr.go((Mark)c); break;
			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
			default: System.out.println("Error typename invalid");
			}
			
			index++;
			
		}
	}
	
	public void goBlob(Blob comp, Mat img) {
    	img = filter.removeOutline2(img); 
		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		comp.image = img;
    }
	
}
