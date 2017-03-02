package segmentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import fields.Text;
import filesaving.FileSave;
import forms.Form;
import helpers.ComputerVision;
import helpers.Filtering;
import helpers.Sorting;

public class TextSegmentation {

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static FileSave fs = FileSave.getInstance();

    public final int MIDDLE_OFFSET_Y = 10;

    private static TextSegmentation textSeg = new TextSegmentation();
    public static TextSegmentation getInstance() { return textSeg; }
    private TextSegmentation() { }
    
    public List<Mat> erodeImages(List<Mat> images) {
    	int size = images.size();
    	Mat temp;
    	List<Mat> adjImages = new ArrayList<>();
    	for(int i = 0; i < size; i++) {
    		temp = images.get(i);
    		cv.invert(temp);
			cv.morph(temp, Imgproc.MORPH_ERODE, Imgproc.MORPH_RECT, 5);
			adjImages.add(temp);
    	}
    	return adjImages;
    }

    public List<Mat> dilateImages(List<Mat> images) {
    	int size = images.size();
    	Mat temp;
    	List<Mat> adjImages = new ArrayList<>();
    	for(int i = 0; i < size; i++) {
    		temp = images.get(i);
			cv.morph(temp, Imgproc.MORPH_DILATE, Imgproc.MORPH_RECT, 5);
			adjImages.add(temp);
    	}
    	return adjImages;
    }

    public void segment(Mat groupImage, Form form, int groupIndex) {
		List<Object> group = form.getGroups().get(groupIndex);

		List<MatOfPoint> textContours = cv.findContours(groupImage.clone(), Imgproc.RETR_EXTERNAL);
		
		textContours = filterElements(textContours, form.getElementCount()[groupIndex]);
		List<Mat> textImages = getImages(groupImage, textContours);
		
		List<MatOfPoint> letterContours;
		Mat textImage, textImageInv, temp;
		int letterCount;
		
		int size = textImages.size();
		for(int textIndex = 0; textIndex < size; textIndex++) {
			letterCount = ((Text)group.get(textIndex)).getLetterCount();
			textImage = textImages.get(textIndex).clone();
			textImageInv = textImage.clone();

    		cv.invert(textImageInv);
			cv.morph(textImageInv, Imgproc.MORPH_ERODE, Imgproc.MORPH_RECT, 5);
			letterContours = cv.findContours(textImageInv.clone(), Imgproc.RETR_EXTERNAL);
			letterContours = filterMidYContours(letterContours, letterCount, textImage.rows()/2, textImage, "_" + groupIndex + "_" + textIndex);	
			temp = redrawContours(letterContours, textImage.rows(), textImage.cols());
			cv.morph(temp, Imgproc.MORPH_DILATE, Imgproc.MORPH_RECT, 5);
	    	letterContours = cv.findContours(temp, Imgproc.RETR_EXTERNAL);
	    	letterContours = filterElements(letterContours, letterCount);
	    	List<Mat> letterImages = filter.borderRemoval(letterContours, textImage, false);
	    	letterImages = cleanImages(letterImages);
	    	
	    	String folderName = "text_" + groupIndex + "_" + textIndex;
	    	File folder = new File(folderName);
	    	folder.mkdir();
	    	
	    	for(int i = 0; i < letterImages.size(); i++) {
	    		Imgcodecs.imwrite(folderName + File.separator + i + ".png", letterImages.get(i));
	    	}
		}
    }
    
    public List<Mat> cleanImages(List<Mat> letterImages) {
    	int size = letterImages.size();
    	for(int i = 0; i < size; i++) {
    		cv.morph(letterImages.get(i), Imgproc.MORPH_OPEN, Imgproc.MORPH_ELLIPSE, 3);
    		cv.morph(letterImages.get(i), Imgproc.MORPH_CLOSE, Imgproc.MORPH_ELLIPSE, 3);
    	}
    	return letterImages;
    }
    
    public Mat redrawContours(List<MatOfPoint> letterContours, int rows, int cols) {
    	Scalar white = new Scalar(255);
    	int size = letterContours.size();
		Mat drawnMat = Mat.zeros(rows, cols, CvType.CV_8UC1);
		
    	for(int i = 0; i < size; i++)
    		Imgproc.drawContours(drawnMat, letterContours, i, white);
    	
    	return drawnMat;
    	
    }
    
    public List<MatOfPoint> filterTextboxes(List<MatOfPoint> contours, int elementCount) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	contours2 = getLargestContours(contours2, elementCount);
    	contours2 = sort.contourPositions(contours2);
    	
     	return contours2;
    }

    public List<MatOfPoint> segmentLetterboxes(List<MatOfPoint> contours, int elementCount) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	contours2 = getLargestContours(contours2, elementCount);
    	contours2 = sort.contourPositions(contours2);
    	return contours2;
    }

    public List<MatOfPoint> filterElements(List<MatOfPoint> contours, int elementCount) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	contours = sort.contourAreas(contours, sort.ORDER_DESC);
    	contours2 = contours.subList(0, elementCount);
    	contours2 = sort.contourPositions(contours2);
    	return contours2;
    }
    
    public List<MatOfPoint> getLargestContours(List<MatOfPoint> contours, int elementCount) {
    	contours = sort.contourAreas(contours, sort.ORDER_DESC);
    	return contours.subList(0, elementCount);
    }
    
    public List<Mat> getImages(Mat image, List<MatOfPoint> elementContours) {
    	List<Mat> elements = new ArrayList<>();
    	int size = elementContours.size();
    	for(int i = 0; i < size; i++) {
    		elements.add(getSubImage(image, elementContours.get(i)));
    	}
    	return elements;
    }

    public Mat getSubImage(Mat image, MatOfPoint contour) {
    	Rect contourRect = Imgproc.boundingRect(contour);
    	return image.submat(contourRect);
    }
    
    public List<MatOfPoint> filterMidYContours(List<MatOfPoint> contours, int elementCount, int midY, Mat subImage, String name) {
    	List<MatOfPoint> contours2 = new ArrayList<>();
    	Rect rect;
    	int size = contours.size();
    	int lowerBound = midY - MIDDLE_OFFSET_Y;
    	int upperBound = midY + MIDDLE_OFFSET_Y;
    	int mid;

    	Mat newMat = Mat.zeros(subImage.rows(), subImage.cols(), CvType.CV_8UC3);
    	Imgproc.line(newMat, new Point(0, midY), new Point(newMat.width(), midY), new Scalar(0, 255, 0));
    	Imgproc.line(newMat, new Point(0, lowerBound), new Point(newMat.width(), lowerBound), new Scalar(255, 255, 0));
    	Imgproc.line(newMat, new Point(0, upperBound), new Point(newMat.width(), upperBound), new Scalar(255, 255, 0));
    	
    	for(int i = 0; i < size; i++) {
    		rect = Imgproc.boundingRect(contours.get(i));
    		mid = (int) ( rect.y + (rect.height / 2) );
    		int mid1 = (int) ( rect.x + (rect.width / 2) );
    		int mid2 = (int) ( rect.y + (rect.height / 2) );
    		if(mid >= lowerBound && mid <= upperBound)
    			contours2.add(contours.get(i));
    		else {
//    			System.out.println(" ---> " + midY);
//    			System.out.println(" ---< " + mid);
    		}
        	Imgproc.circle(newMat, new Point(mid1, mid2), 5, new Scalar(255, 255, 255));

    	}
//    	Imgcodecs.imwrite(name + ".png", newMat);
    	return contours2;
    }
    
}
