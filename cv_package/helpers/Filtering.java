package cv_package.helpers;

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

import cv_package.basicelem2.Blob;
import cv_package.dumps.Folder;

public class Filtering {

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Folder folder = Folder.getInstance();

    private static Filtering filter = new Filtering();
    public static Filtering getInstance() { return filter; }
    private Filtering() { }
    
    public Mat removeSmallContours(Mat image, List<MatOfPoint> smallContours) {
    	Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
    	Mat image3 = new Mat();
    	Scalar black = new Scalar(0);
    	
    	int i = 0;
    	for(MatOfPoint c:smallContours) {
    		Imgproc.drawContours(image2, smallContours, i, black, -1);
    		i++;
    	}
    	
    	Core.multiply(image, image2, image3);
    	
    	return image3;
    }
    
    public Mat removeOutlineFinal(Mat image) {
		cv.invert(image);
		folder.save(image);
		drawBorder(image, new Scalar(255), 5);
		folder.save(image);
		return removeOutline2(image);
    }
    
    public void drawBorder(Mat image, Scalar color, int thickness) {
    	Imgproc.rectangle(image, new Point(0,0), new Point(image.cols()-1, image.rows()-1), color, thickness);
    }
    
    public ArrayList<Mat> divideImage(Mat image, int count) {
    	ArrayList<Mat> images = new ArrayList<>();
    	
		images = (ArrayList<Mat>) largeAreaElements(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), count);
		
//		for(Mat m:images) {
//			invert(m);
//		}
		
    	return images;
    }
    
//    public void blob(Blob comp) {
////    	Imgcodecs.imwrite("C:/Users/Hannah/Desktop/ex/____her!!.png", comp.image);
//    	Mat img = comp.image;
//    	img = removeOutline2(comp.image); 
//		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
//		comp.image = img;
//    }
    
    public Mat draw(Mat image, List<MatOfPoint> contours) {
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
		
		for(int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(image2, contours, i, new Scalar(0));
			
			Rect r = Imgproc.boundingRect(contours.get(i));
			Imgproc.putText(image2, ""+i, new Point(r.x,r.y), Core.FONT_HERSHEY_PLAIN, 1, new Scalar(0));
		}
		
		folder.save(image2);
		
		return image2;
    }
    
    public Mat draw(Mat image, List<MatOfPoint> contours, int i) {
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
		
		Imgproc.drawContours(image2, contours, i, new Scalar(0));
		
		return image2;
    }
    
    public List<Mat> largeAreaElements(Mat mainImage, List<MatOfPoint> contours, int elementCount) {
		List<MatOfPoint> contours2 = new ArrayList<>(contours);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
//		System.out.println("contours-"+contours2.size());
		contours2 = contours.subList(0, elementCount);
		contours2 = sort.contourPositions(contours2);
//		draw(mainImage, contours2, 321);
//		folder.save(draw(mainImage, contours2));
		
		return getImages(mainImage, contours2);
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
	
	public Mat removeBackground(Mat image) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_LIST);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
		Mat image3 = new Mat(image.size(), image.type());
		
		// Draw black colored contour on white image
		Imgproc.drawContours(image2, contours, 0, new Scalar(0));
		// Fill contour with black color
		Imgproc.floodFill(image2, new Mat(), new Point(image2.cols()/2, image2.rows()/2), new Scalar(0));
		// original - new image = white colored character with black background
		Core.subtract(image, image2, image3);
		
		Imgcodecs.imwrite("Tests/OCR/5_1.jpg", image);
		Imgcodecs.imwrite("Tests/OCR/5_2conts.jpg", image2);
		Imgcodecs.imwrite("Tests/OCR/5_3nomore.jpg", image3);
		
		return image3;
	}
	
	// for blob outlines
	public Mat removeOutline2(Mat image) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_LIST);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		contours = contours.subList(0, 2);
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(0));
		Mat image3 = new Mat(image.size(), image.type());
		Mat ones = new Mat(image.size(), image.type(), new Scalar(255));

		Imgproc.drawContours(image2, contours, 1, new Scalar(255));
		folder.save(image2, "contours");

		Imgproc.floodFill(image2, new Mat(), new Point(image2.cols()/2, image2.rows()/2), new Scalar(255));
		folder.save(image2, "flood");

		Core.multiply(image, image2, image3);
		Imgproc.drawContours(image3, contours, 1, new Scalar(0), 5);
		folder.save(image3, "mul + draw");
		
//		image3 = getSubImage(image3, contours.get(1));

//		folder.save(image3, "result");
		
//			Imgcodecs.imwrite("Tests/_border-1.jpg", image);
//			Imgcodecs.imwrite("Tests/_border-2conts.jpg", image2);
//			Imgcodecs.imwrite("Tests/_border-3nomore.jpg", image3);
		
		return image3;
	}
	
	// for normal outlines
	public Mat removeOutline(Mat image) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_LIST);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		contours = contours.subList(0, 2);
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
		Mat image3 = new Mat(image.size(), image.type());
		
		// Draw black colored contour on white image
		Imgproc.drawContours(image2, contours, 1, new Scalar(0));
		folder.save(image2,"contour");
		// Fill contour with black color
		Imgproc.floodFill(image2, new Mat(), new Point(image2.cols()/2, image2.rows()/2), new Scalar(0));
		folder.save(image2,"filled");
		// original + new image = main object with white background
		Core.add(image, image2, image3);
		folder.save(image3,"add");
		
		image3 = getSubImage(image3, contours.get(1));
		folder.save(image3);
		
//		Imgcodecs.imwrite("Tests/_border-1.jpg", image);
//		Imgcodecs.imwrite("Tests/_border-2conts.jpg", image2);
//		Imgcodecs.imwrite("Tests/_border-3nomore.jpg", image3);
		
		return image3;
	}
	
	// for outlines with chars inside - matstrings to matchars
	public Mat removeOutline(Mat image, int charBoxCnt) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_LIST);
		contours = sort.contourAreas(contours, sort.ORDER_DESC);
		contours = contours.subList(0, charBoxCnt+1);
		Mat image2 = new Mat(image.size(), image.type(), new Scalar(255));
		Mat image3 = new Mat(image.size(), image.type());

		// Draw black colored contour on white image
		// Fill contour with black color
		
		for(int i = 1; i <= charBoxCnt; i++) {
			Imgproc.drawContours(image2, contours, i, new Scalar(0));
			Rect br = Imgproc.boundingRect(contours.get(i));
			Imgproc.floodFill(image2, new Mat(), new Point(br.x+br.width/2, br.y+br.height/2), new Scalar(0));
		}
		// original + new image = main object with white background
		Core.add(image, image2, image3);
		
		// cropping out the extra space
		contours = contours.subList(1, charBoxCnt+1);
		contours = sort.contourPositions(contours);
		Rect br1 = Imgproc.boundingRect(contours.get(0));
		Rect br2 = Imgproc.boundingRect(contours.get(charBoxCnt-1));
		int rowS, rowE, colS, colE;
		colS = br1.x;
		colE = br2.x+br2.width;
		if(br1.y > br2.y) rowS = br1.y;
		else rowS = br2.y;
		if(br1.y+br1.height > br2.y+br2.height) rowE = br1.y+br1.height;
		else rowE = br2.y+br2.height;
		image3 = image3.submat(rowS, rowE, colS, colE);
//		image3 = getSubImage(image3, contours.get(1));
		
//		Imgcodecs.imwrite("Tests/_ocr-1.jpg", image);
//		Imgcodecs.imwrite("Tests/_ocr-2conts.jpg", image2);
//		Imgcodecs.imwrite("Tests/_ocr-3nomore.jpg", image3);
		
		return image3;
	}
	
    public List<Mat> borderRemoval(List<MatOfPoint> borderContours, Mat image, boolean isOuterBorder) {	//
    	// IF outer border, still need to get inner border
    	
    	List<Mat> resultImages = new ArrayList<>();
    	int size = borderContours.size();
    	Mat filled = new Mat(image.rows(), image.cols(), CvType.CV_8UC1, new Scalar(255));
    	Scalar black = new Scalar(0);
    	MatOfPoint border;
    	Rect rect;
    	int midX, midY;
    	Mat matA, matB, result;
    	
    	for(int i = 0; i < size; i++) {

    		border = borderContours.get(i);
    		rect = Imgproc.boundingRect(border);
        	matA = image.submat(rect);
    		
    		if(isOuterBorder) {
//	    		 getting inner border
	            Mat inv = image.submat(rect);
	            List<MatOfPoint> invCont = cv.findContours(inv.clone(), Imgproc.RETR_EXTERNAL);
	            invCont = getLargestN(invCont, 1);
	    		
	            // fill
	            filled = new Mat(rect.height, rect.width, CvType.CV_8UC1, new Scalar(255));
	    		Imgproc.drawContours(filled, invCont, 0, black);
	    		midX = rect.width/2;
	    		midY = rect.height/2;
	            Imgproc.floodFill(filled, new Mat(), new Point(midX, midY), black);
	            
	            // black inside with white outside border
	        	matB = filled;
    			
	    	}
    		else {
    			
        		// fill
        		Imgproc.drawContours(filled, borderContours, i, black);
        		midX = rect.x + rect.width/2;
        		midY = rect.y + rect.height/2;
                Imgproc.floodFill(filled, new Mat(), new Point(midX, midY), black);

                // black inside with white outsideS border
            	matB = filled.submat(rect);
            	cv.invert(matA);
    		}
    		
        	
    		result = new Mat(matA.rows(), matA.cols(), CvType.CV_8UC1);

        	// add - removal of border
            Core.add(matA, matB, result);
            
            if(isOuterBorder)
            	cv.invert(result);
//            else 
//        		Imgcodecs.imwrite("asd" + File.separator  + "_ltr_" + i + ".png", result);

//			Imgcodecs.imwrite("w" + i + "_0filled.png", matB);
//			Imgcodecs.imwrite("w" + i + "_1normal.png", matA);
//			Imgcodecs.imwrite("w" + i + "_2result.png", result);
            
            resultImages.add(result);
    	}
    	
    	return resultImages;
    	
    }
    
    public List<MatOfPoint> getLargestN(List<MatOfPoint> contours, int elementCount) {
    	contours = sort.contourAreas(contours, sort.ORDER_DESC);
    	return contours.subList(0, elementCount);
    }

    public void invert(Mat image) {
    	Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV);
    }
    
}
