package cv_package.main;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import cv_package.debug.Printer;
import cv_package.debug.Saver;
import cv_package.fields.Text;
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
		Mat image = Imgcodecs.imread("input"+File.separator+"figure1.jpg");
		
				
		Form form = new FormPatientInfo();
		
		form.setGroupCount(6);
    	
    	int[] guideMatch = {1, 2, 3, 4, 5, 5, 6};
    	form.setGuideMatch(guideMatch);
    	form.setGroupCount(7);
    	int[] fieldTypes = {1, 1, 3, 3, 2, 2, 3};
    	form.setGroupTypes(fieldTypes);
    	
    	//form.totalContours = guideCount + getGroupCount();
    	int[] elementCount = {3, 9, 1, 1, 7, 8, 1};
    	form.setElementCount(elementCount);
		
    	
    	ArrayList<Object> basicDetails = new ArrayList<>();
    	basicDetails.add(new Text("Health Center", 6));
    	basicDetails.add(new Text("Healthcare Personnel ID", 12));
    	basicDetails.add(new Text("Date", 6));
    	
    	ArrayList<Object> patientDetails = new ArrayList<>();
    	patientDetails.add(new Text("Patient Name", 25));
    	patientDetails.add(new Text("PhilHealth Number", 14));
    	patientDetails.add(new Text("Date of Birth", 6));
    	patientDetails.add(new Text("Gender", 1));
    	patientDetails.add(new Text("Marital Status", 1));
    	patientDetails.add(new Text("Blood Type", 3));
    	patientDetails.add(new Text("Address", 27));
    	patientDetails.add(new Text("Cellphone Number", 11));
    	patientDetails.add(new Text("Guardian Name", 25));

//
//
    	form.setGroups();
    	form.addGroup(basicDetails);
    	form.addGroup(patientDetails);
    	
    	
    	
		form.setImage(image);
		Segmentation s = new Segmentation(new Saver(), new Printer());
		s.segment(form);
		System.out.println("[END]");
	}
	
	
	
}
