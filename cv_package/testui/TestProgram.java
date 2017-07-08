package cv_package.testui;

import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;

import cv_package.debug.LocalPrinter;
import cv_package.debug.Printer;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.testgen.CharacterClassifier;
import cv_package.testgen.Segmenter3;
import cv_package.testgen.TemporaryClassifier;

public class TestProgram {

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
//	private static Folder folder = Folder.getInstance();
//	private static Images images = Images.getInstance();
//	private static Error error = Error.getInstance();
//	private static Time time = Time.getInstance();
//	private static OCR ocr = OCR.getInstance();
	
	public static void main(String args[]) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		System.out.println("start..");
		
		TestProgram t = new TestProgram();
		t.test();
		
		System.out.println("done..");
	}
	
	public void test2() {
		Mat image = Imgcodecs.imread("C:/Users/Hannah/Desktop/temp/1.png");
		cv.preprocess(image);
		Imgcodecs.imwrite("C:/Users/Hannah/Desktop/temp/2preproc.png", image);
		
		ArrayList<MatOfPoint> contours = cv.findContoursExt(image);
		Imgcodecs.imwrite("C:/Users/Hannah/Desktop/temp/3contours.png", filter.draw(image, contours));
		
		cv.invert(image);
		Mat cleanImage = filter.cleanBackground(image, contours);
		Imgcodecs.imwrite("C:/Users/Hannah/Desktop/temp/4clean.png", cleanImage);

	}
	
	public void test() throws IOException {
		Segmenter3 s = new Segmenter3();
		String filepath = "input/form1.jpg";
//		String structpath = "C:/Users/Hannah/Desktop/form-B.txt";
//				String filepath = "C:/Users/Hannah/Desktop/ft6.jpg";
//				String structpath = "C:/Users/Hannah/Desktop/form.txt";
		boolean isSaving = false;
		LocalPrinter printer = new Printer();
		CharacterClassifier classifier = new TemporaryClassifier();
		s.init(filepath, isSaving, printer, classifier);
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
