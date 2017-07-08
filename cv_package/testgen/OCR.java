/*
package cv_package.testgen;
import cv_package.basicelem2.Text;
import cv_package.dumps.Folder;
import cv_package.dumps.Time;

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
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package. helpers.Sorting;

public class OCR{

	//private static HandwrittenDigitClassifier digitClassifier = HandwrittenDigitClassifier.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static Folder folder = Folder.getInstance();
	private static Time time = Time.getInstance();
	
	public final int MIDDLE_OFFSET_Y = 10;
	public final int NEG_VALUE = -1;

	private static int CHAR_LEN = 28;
	private static Size CHAR_SIZE = new Size(CHAR_LEN, CHAR_LEN);
	private static int STANDARD_LEN = 50;
	private static Size STANDARD_RESIZE = new Size(STANDARD_LEN, STANDARD_LEN);
	private static int STARTING_POINT = STANDARD_LEN - CHAR_LEN;

    private static OCR ocr = new OCR();
    public static OCR getInstance() { return ocr; }
    private OCR() { } 
      
    public String gooString(Text comp) throws IOException {
    	ArrayList<Mat> characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount));
		String numStr = "";
		
		for(Mat c:characterMats) {
			String digit;
			if(c == null) {
				digit = "~";
			}
			else {
				String imagepath = folder.save(c);
				digit = digitClassifier.classify(imagepath) + "";
			}
//			time.stamp("digit: " + digit);
			numStr += digit;
		}
		
		return numStr;
	}
    
    public ArrayList<Integer> gooArray(Text comp) throws IOException {
		ArrayList<Mat> characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount));
		ArrayList<Integer> digits = new ArrayList<>();
		
		for(Mat c:characterMats) {
			int digit;
			if(c == null) {
				digit = NEG_VALUE;
			}
			else {
				String imagepath = folder.save(c);
				digit = digitClassifier.classify(imagepath);
			}
			digits.add(digit);
		}
		
		return digits;
	}
    
    public long goo(Text comp) throws IOException {
		ArrayList<Mat> characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount));
		String numStr = "";
		
		for(Mat c:characterMats) {
			String digit;
			if(c == null) {
				digit = "~";
			}
			else {
				String imagepath = folder.save(c);
				digit = digitClassifier.classify(imagepath) + "";
			}
//			time.stamp("digit: " + digit);
			numStr += digit;
		}
		
		long number = Long.parseLong(numStr);
		return number;
	}
    
    public int go(Mat img, int characterCount) throws IOException {
		ArrayList<Mat> characterMats = new ArrayList<>(getMatListCharsClean(img, characterCount));
		String numStr = "";
		
		for(Mat c:characterMats) {
			String imagepath = folder.save(c);
			String digit = digitClassifier.classify(c) + "";
			time.stamp("digit: " + digit);
			numStr += digit;
		}
		
		int number = Integer.parseInt(numStr);
		return number;
	}
    
	public void go(Text comp) {
	
		comp.characterMats = new ArrayList<>(getMatListCharsClean(comp.image, comp.characterCount));
		// recognize
	}
	
	public boolean hasCharacter(List<MatOfPoint> contours) {
		boolean hasChar = true;
		double thresh = 10.0;
		
		if(contours.size() == 0) {
			time.stamp("No contours");
			return false;
		}
		
		MatOfPoint largest = contours.get(0);
		double largestArea = Imgproc.contourArea(largest);
		
		if(largestArea < thresh) {
			time.stamp("large contour small: " + largestArea);
			return false;
		}
		
//		String areasStr = "char object areas: ";
//		for(MatOfPoint c:contours) {
//			double a = Imgproc.contourArea(c);
//			areasStr += a + " / ";
//		}
//		time.stamp(areasStr);
		
		return hasChar;
	}
	
	
	
	

	// MAT (output) contains a char - biggest contour in its box - A
	/*
	public Mat getMatChar(Mat image) {
		image = filter.removeBackground(image);
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);	
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		Rect charRect = Imgproc.boundingRect(contours.get(0));
		return image.submat(charRect);
	}

	*/

/*
	public boolean isCharacter(MatOfPoint largestContour) {
		boolean isChar = true;
		double thresh = 10.0;
		
		double largestArea = Imgproc.contourArea(largestContour);
		
		if(largestArea < thresh) {
			time.stamp("large contour small: " + largestArea);
			return false;
		}
		
		return isChar;
	}
	
	public Mat modifyChar(MatOfPoint charContour, Mat image) {
		Rect charRect = Imgproc.boundingRect(charContour);
		
		int imgH = image.rows(), imgW = image.cols();
		int padding;
		int border = 7;
		int cH = charRect.height, cW = charRect.width;
		int cX = charRect.x, cY = charRect.y;
		int rowS, rowE, colS, colE;

		rowS = cY;
		rowE = cY+cH;
		colS = cX;
		colE = cX+cW;
		
		if(cH > cW) {
			padding = (cH-cW)/2;
			colS -= padding;
			colE += padding;
		}
		else if(cH < cW) {
			padding = (cW-cH)/2;
			rowS -= padding;
			rowE += padding;
		}

		rowS -= border;
		rowE += border;
		colS -= border;
		colE += border;
		
		if(rowS < 0) rowS = 0;
		if(colS < 0) colS = 0;
		if(rowE >= imgH) rowE = imgH;
		if(colE >= imgW) colE = imgW;
		
		image = image.submat(rowS, rowE, colS, colE);
		
		Imgproc.resize(image, image, CHAR_SIZE);
		
		Imgproc.morphologyEx(image, image, Imgproc.MORPH_CLOSE, Mat.ones(2, 2, CvType.CV_8UC1));
		
		return image;
	}
	
	public List<MatOfPoint> getCharContours(Mat image) {
		Mat image2 = image.clone();
		filter.drawBorder(image2, new Scalar(0), 1);
		
		List<MatOfPoint> contours = cv.findContours(image2.clone(), Imgproc.RETR_EXTERNAL);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		
		if(contours.size() == 0)
			return null;
		
		MatOfPoint largestContour = contours.get(0);
		if(isCharacter(largestContour))	return contours;
		else return null;
	}
	
	public Mat getCleanChar(Mat image, ArrayList<MatOfPoint> contours) {
		Scalar black = new Scalar(0), white = new Scalar(255);

		folder.save(image, "IMAGE1!");

		Mat image2 = new Mat(image.size(), image.type());
		image2 = filter.drawAndFillContour(image2, contours, 0, white, black);
		folder.save(image2, "IMAGE2!");
		
		Mat image3 = new Mat(image.size(), image.type());
		Core.multiply(image, image2, image3);
		folder.save(image3, "IMAGE3!");
		
		return image3;
	}
	
	// MAT (output) contains a char - biggest contour in its box - A
	public Mat getMatChar(Mat image) {
		image = filter.removeBackground(image);
//		folder.save(image, "remove bg");
		
		//

//		Mat imagex = new Mat();
//		Imgproc.resize(image, imagex, STANDARD_RESIZE);
//		
//		ArrayList<MatOfPoint> contoursx = new ArrayList<>(getCharContours(imagex.clone()));
//		
//		if(contoursx == null)
//			return null;
//		
//		Mat cleanChar = getCleanChar(imagex, contoursx);
//
//		MatOfPoint charContour = contoursx.get(0); 
//
//		cv.morph(cleanChar, Imgproc.MORPH_ERODE, Imgproc.MORPH_ELLIPSE, 2);
//		
//		folder.save(cleanChar, "morph");
//		
//		cleanChar = modifyChar(charContour, cleanChar);
//		
//		folder.save(cleanChar, "final");
//		
//		return cleanChar;
		
//		//
//		
		Mat image2 = new Mat();
		Size size = new Size(50,50);
		Imgproc.resize(image, image2, size);
		
		filter.drawBorder(image2, new Scalar(0), 1);
		
		List<MatOfPoint> contours = cv.findContours(image2.clone(), Imgproc.RETR_EXTERNAL);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		
		
		if(hasCharacter(contours)) {
//			Mat skel = filter.draw(image2, contours);
//			folder.save(skel, "contour");
//			List<MatOfPoint> smallContours = contours.subList(1, contours.size());
//			image = filter.removeSmallContours(image2, smallContours);
					
			image = modifyChar(contours.get(0), image2);
			folder.save(image, "done");
		}
		else {
			image = null;
		}
		
		return image;
	}
	
	// MAT (output) contains a boxed char - [ A ]
	public List<Mat> getMatListChar(Mat image, int charBoxCnt) {
		image = filter.removeOutline(image, charBoxCnt); 	
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		List<Mat> matChars = null;
		matChars = filter.largeAreaElements(image, cv.findContours(image.clone(), Imgproc.RETR_LIST), charBoxCnt);
		
		return matChars;
	}
	
	// MAT (output) contains a char - A
	public List<Mat> getMatListCharsClean(Mat image, int charBoxCnt) {

		Mat x = image.clone();
//		cv.invert(x);
//		folder.save(x, "invert");

		filter.drawBorder(x, new Scalar(0), 1);
//		folder.save(x, "drawborder");
		x = filter.removeOutlineChar(x); 
		folder.save(x, "removeoutline X");
		
//		image = filter.removeOutline(image, charBoxCnt); 	
//		cv.invert(image);
//		folder.save(image, "removeoutline Y");
		
		List<Mat> matChars = null;
		matChars = filter.largeAreaElements(x, cv.findContours(x.clone(), Imgproc.RETR_LIST), charBoxCnt);
		List<Mat> matCharsClean = new ArrayList<>();

		int i = 0;
		for(Mat m:matChars) {
//			folder.save(m, "before");
			Mat matC = getMatChar(m);
			matCharsClean.add(matC);
//			folder.save(matC, "after");

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
*/