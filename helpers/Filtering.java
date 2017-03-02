package helpers;

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

public class Filtering {

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	
    private static Filtering filter = new Filtering();
    public static Filtering getInstance() { return filter; }
    private Filtering() { }
    
    public List<Mat> borderRemoval(List<MatOfPoint> borderContours, Mat image, boolean isOuterBorder) {
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
    
}
