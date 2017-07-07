package cv_package.testgen;

import java.awt.Image;
import cv_package.basicelem2.Text;

//import android.graphics.Bitmap;
//import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.filesaving.FileSave;
import cv_package.helpers.BorderHandler;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package. helpers.Sorting;

public class OCR2{

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static FileSave fs = FileSave.getInstance();
	public final int MIDDLE_OFFSET_Y = 10;
	
	private static int CHAR_LEN = 30;
	private static Size CHAR_SIZE = new Size(CHAR_LEN, CHAR_LEN);

    private static OCR2 ocr = new OCR2();
    public static OCR2 getInstance() { return ocr; }

	
	public void go(Text comp, Rect rect, Mat Location) {
		
		comp.characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount, rect , Location));
		// recognize
	}
	
	
	
	// MAT (output) contains a char - biggest contour in its box - A
	public Mat getMatChar2(Mat image) {
		image = filter.removeBackground(image);
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);	
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		Rect charRect = Imgproc.boundingRect(contours.get(0));
		
		int padding;
		int cH = charRect.height, cW = charRect.width;
		int cX = charRect.x, cY = charRect.y;
		int rowS, rowE, colS, colE;
		
		if(cH > cW) {
			padding = (cH-cW)/2;
			rowS = cY;
			rowE = cY+cH;
			colS = cX-padding;
			colE = cX+cW+padding;
		
			image.adjustROI(-padding, -padding, -padding, -padding);
//			System.out.println("> " + (rowE-rowS) + ", " + (colE-colS));
		}
		
		
		else if(cH < cW) {
			padding = (cW-cH)/2;
			rowS = cY-padding;
			rowE = cY+cH+padding;
			colS = cX;
			colE = cX+cW;
		
			image.adjustROI(-padding, -padding, -padding, -padding);
//			System.out.println("> " + (rowE-rowS) + ", " + (colE-colS));
		}
		else {
			image = image.submat(charRect);
		}
		
		Imgproc.resize(image, image, CHAR_SIZE);
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
		return image;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// MAT (output) contains a char - biggest contour in its box - A
	public Mat getMatChar(Mat image) {
		image = filter.removeBackground(image);
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);	
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		Rect charRect = Imgproc.boundingRect(contours.get(0));
		
		int padding;
		int cH = charRect.height, cW = charRect.width;
		int cX = charRect.x, cY = charRect.y;
		int rowS, rowE, colS, colE;
		
		if(cH > cW) {
			padding = (cH-cW)/2;
			rowS = cY;
			rowE = cY+cH;
			colS = cX-padding;
			colE = cX+cW+padding;
			image = image.submat(rowS, rowE, colS, colE);
//			System.out.println("> " + (rowE-rowS) + ", " + (colE-colS));
		}
		
		
		else if(cH < cW) {
			padding = (cW-cH)/2;
			rowS = cY-padding;
			rowE = cY+cH+padding;
			colS = cX;
			colE = cX+cW;
			image = image.submat(rowS, rowE, colS, colE);
//			System.out.println("> " + (rowE-rowS) + ", " + (colE-colS));
		}
		else {
			image = image.submat(charRect);
		}
		
		Imgproc.resize(image, image, CHAR_SIZE);
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
		return image;
	}
	
	// MAT (output) contains a boxed char - [ A ]
	public List<Mat> getMatListChar(Mat image, int charBoxCnt) {
		image = filter.removeOutline(image, charBoxCnt); 						Imgcodecs.imwrite("Tests/OCR/1 NO BORDER.jpg", image);
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV); 	Imgcodecs.imwrite("Tests/OCR/2 NO BORDER - INV.jpg", image);
		List<Mat> matChars = null;
		matChars = filter.largeAreaElements(image, cv.findContours(image.clone(), Imgproc.RETR_LIST), charBoxCnt);
		
		return matChars;
	}
	
	// MAT (output) contains a char - A
	public List<Mat> getMatListCharsClean(Mat image, int charBoxCnt, Rect rect, Mat location) {
		BorderHandler border = new BorderHandler();
		Mat s = border.getBorders(image, charBoxCnt, 2);
		
		Imgcodecs.imwrite("Thisissubmat.jpg",s);
		
		//s = filter.removeOutline(s, charBoxCnt); 	
		Imgcodecs.imwrite("nomoreOutline.jpg", s);
//		Imgcodecs.imwrite("Tests/OCR2/LKASDJ.jpg", image);
//		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV); 	
//		Imgcodecs.imwrite("Tests/OCR2/X NO BORDER - INV.jpg", image);
		List<Mat> matChars = null;
		
		
		List<MatOfPoint> contours = cv.findContours(s, Imgproc.RETR_LIST);
		Mat filled = new Mat(image.rows(), image.cols(), CvType.CV_8UC1, new Scalar(0));
		System.out.println("ContourSize "+contours.size());
		
		//contours = filter.getLargestN(contours,charBoxCnt+1);
		//Imgproc.drawContours(filled, contours, 2, new Scalar(255));
		contours.remove(0);
		contours = sort.contourPositions(contours);
		
		
		
		Imgcodecs.imwrite("drawmyown.jpg",filled);
		Imgcodecs.imwrite("drawmyown2.jpg",image);
		
		//matChars = filter.largeAreaElements(image, contours, charBoxCnt);
		matChars = filter.getImages(image, contours);
		
		
		List<Mat> matCharsClean = new ArrayList<>();
		Mat m;
		Imgcodecs.imwrite(0+"hello2.jpg",matChars.get(0));
		Imgcodecs.imwrite(1+"hello2.jpg",matChars.get(1));
		//System.out.println("MatChars size "+ matChars.size()); 
		for(int i=0;i<matChars.size(); i++){
			m = matChars.get(i);
			Imgcodecs.imwrite(i+"hello.jpg",m);
			matCharsClean.add(getMatChar2(m));
		}
				
		return matCharsClean;
	}
	
	// MAT (output) contains set of boxed chars - [ A ] [ B ] [ C ]
	public List<Mat> getMatListStrings(Mat image, int stringBoxCnt) {
		List<Mat> matStrings = null;
		matStrings = filter.largeAreaElements(image, cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), stringBoxCnt);
		
		return matStrings;
	}
}
