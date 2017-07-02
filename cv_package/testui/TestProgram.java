package cv_package.testui;

import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.dumps.*;
import cv_package.dumps.Error;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.testgen.HandwrittenDigitClassifier;
import cv_package.testgen.OCR;
import cv_package.testgen.Segmenter2;

public class TestProgram {

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static Folder folder = Folder.getInstance();
	private static Images images = Images.getInstance();
	private static Error error = Error.getInstance();
	private static Time time = Time.getInstance();
	private static OCR ocr = OCR.getInstance();
	
	public static void main(String args[]) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		System.out.println("start..");
		
		TestProgram t = new TestProgram();
		t.test();
		
		System.out.println("done..");
	}
	
	public void test() throws IOException {
		Segmenter2 s = new Segmenter2();
		String filepath = "C:/Users/Hannah/Desktop/ft5.jpg";
		String structpath = "C:/Users/Hannah/Desktop/form.txt";
		s.init(filepath, structpath);
		s.segment();
		
//		folder.init();
//		time.start();
//		Mat img = Imgcodecs.imread("C:/Users/Hannah/Desktop/25.png");
//		folder.save(img);
//		cv.preprocess(img);
//		folder.save(img);
//		Mat c = ocr.getMatChar(img);
//		if(c == null)
//			System.out.println("NO CHAR!");
//		time.end();
	}
		
}
