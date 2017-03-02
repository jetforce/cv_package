package cv_package.main;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import cv_package.forms.Form;
import cv_package.forms.FormPatientInfo;
import cv_package.segmentation.FourSquareCorner;
import cv_package.segmentation.Segmentation;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Imgcodecs.imread("input"+File.separator+"x1.jpg");
		Form form = new FormPatientInfo();
		form.setImage(image);
		Segmentation s = Segmentation.getInstance();
		s.segment(form);
		System.out.println("[END]");
		
	}
	
}
