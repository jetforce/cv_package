package cv_package.basicelem2;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.helpers.BorderHandler;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.segmentation.TableSegmentation;
import cv_package.testgen.OCR;
import cv_package.testgen.OCR2;
import cv_package.testgen.OMR;
import cv_package.testui.PanelBuilder;
import cv_package.segmentation.OpticalMarkSegmentationv2;
import cv_package.debug.*;

public class Form {

	public ArrayList<Type> components;
	public int count;
	public Mat image;
	//This image contains just the straight lines;
	public Mat LocationImage;
	public ArrayList<Mat> processedImages = new ArrayList<>();
	
	public void process() {
		if(components.size() == 0)
			System.out.println("Error no components");
		else
			go();
	}

	OCR ocr = OCR.getInstance();
	OpticalMarkSegmentationv2  omr = new OpticalMarkSegmentationv2(new Saver());
	TableSegmentation ts = TableSegmentation.getInstance();
	BorderHandler borders = new BorderHandler();
	ComputerVision cv = ComputerVision.getInstance();
	Filtering filter = Filtering.getInstance();

	private void go() {
		System.out.println("Go!"+ image.type());
		//This is my attempt to remove the noise
		// i am failing :---(
		ArrayList<MatOfPoint> contours;
		
		
		Mat justLines  = borders.getBorders(image,30,80);
		LocationImage = justLines;
		Rect tempRect;
		Imgcodecs.imwrite("Helllllo.jpg",justLines);
		
		contours = cv.findContours(justLines, Imgproc.RETR_EXTERNAL);
		List<MatOfPoint> matsContour = filter.largeAreaElementsContours(image.clone(),contours , components.size());
		List<Mat> mats = filter.getImages(image, matsContour);
		
		System.out.println("Components"+ components.size());
		
		/*
		contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
		List<Mat> mats = filter.largeAreaElements
				(image.clone(),contours , components.size());
		System.out.println("Components"+ components.size());
		*/
		
		int index = 0;
		for(Type c:components) {
			
			processedImages.add(mats.get(index));
			
			c.image = mats.get(index);
			System.out.println("Mats type "+ c.image.type());
			Imgcodecs.imwrite("duh"+index+".png", c.image);
	    	System.out.println("HERE "+ c.typename);
	    	
			switch(c.typename) {
			//case "TEXT": 
				//tempRect =  Imgproc.boundingRect(matsContour.get(index));
				//ocr.go((Text)c, tempRect, LocationImage); break;
			case "TEXT": ocr.go((Text)c); break;
			case "MARK": omr.go((Mark)c); break;
			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
			case "TABLE": ts.go((Table) c);break;
			default: System.out.println("Error typename invalid");
			}
			
			index++;
		
		}
		
		
		
	}
	
	public void goBlob(Blob comp, Mat img) {
		//img.convertTo(img, CvType.CV_8UC1);
    	img = filter.removeOutline2(img); 
		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		comp.image = img;
    }
	
}
