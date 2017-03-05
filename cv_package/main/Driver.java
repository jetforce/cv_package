package cv_package.main;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import cv_package.forms.Form;
import cv_package.forms.FormMarks;
import cv_package.forms.FormPatientInfo;
import cv_package.segmentation.Segmentation;
import cv_package.segmentation.FourSquareCorner;

public class Driver {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("This jet main");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Imgcodecs.imread("input"+File.separator+"omr6.jpg");
		
		Imgcodecs.imwrite("wat.jpg", image);
		
		FourSquareCorner corner = new FourSquareCorner();
		corner.Normalize(image,true);
		Form form = new FormMarks();
		form.setImage(image);
		Segmentation s = Segmentation.getInstance();
		//s.segment(form);
		System.out.println("[END]");
	}
	
	
	
	
}
