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
import cv_package.testgen.OMR;
import cv_package.segmentation.OpticalMarkSegmentationv2;
import cv_package.debug.*;

public class Form {

	public ArrayList<Type> components;
	public int count;
	public Mat image;
	//This image contains just the straight lines;
	public Mat LocationImage;
	public ArrayList<Mat> processedImages = new ArrayList<>();
	public int formNumber;
	
	
//	public void process() {
//		if(components.size() == 0)
//			System.out.println("Error no components");
//		else
//			go();
//	}

	OCR ocr = OCR.getInstance();
	OpticalMarkSegmentationv2  omr = new OpticalMarkSegmentationv2(new Saver());
	TableSegmentation ts = TableSegmentation.getInstance();
	BorderHandler borders = new BorderHandler();
	ComputerVision cv = ComputerVision.getInstance();
	Filtering filter = Filtering.getInstance();

	
}
