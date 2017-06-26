package cv_package.testgen;

import java.awt.Image;
import cv_package.basicelem2.Text;

//import android.graphics.Bitmap;
//import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package. helpers.Sorting;

public class OCR{

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static FileSave fs = FileSave.getInstance();
	public final int MIDDLE_OFFSET_Y = 10;
	
	private static int CHAR_LEN = 30;
	private static Size CHAR_SIZE = new Size(CHAR_LEN, CHAR_LEN);

    private static OCR ocr = new OCR();
    public static OCR getInstance() { return ocr; }
    private OCR() { }
	
	public void go(Text comp) {
		comp.characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount));
		// recognize
	}
	
	// MAT (output) contains a char - biggest contour in its box - A
	public Mat getMatChar(Mat image) {
		image = filter.removeBackground(image);
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
		sort.contourAreas(contours, sort.ORDER_DESC);
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
	public List<Mat> getMatListCharsClean(Mat image, int charBoxCnt) {
		image = filter.removeOutline(image, charBoxCnt); 						
//		Imgcodecs.imwrite("Tests/OCR2/LKASDJ.jpg", image);
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV); 	
//		Imgcodecs.imwrite("Tests/OCR2/X NO BORDER - INV.jpg", image);
		List<Mat> matChars = null;
		matChars = filter.largeAreaElements(image, cv.findContours(image.clone(), Imgproc.RETR_LIST), charBoxCnt);
		
		List<Mat> matCharsClean = new ArrayList<>();
		for(Mat m:matChars) {
			matCharsClean.add(getMatChar(m));
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
