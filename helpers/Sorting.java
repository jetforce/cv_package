package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class Sorting {
	
    public final static int TEXTBOX_OFFSET = 20;
    public final boolean ORDER_ASC = true;
    public final boolean ORDER_DESC = false;
	
    private static Sorting cv = new Sorting();
    public static Sorting getInstance() { return cv; }
    private Sorting() { }
    
    // Position Sorting
    
    public List<MatOfPoint> contourPositions(List<MatOfPoint> contours) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	Collections.sort(contours2, positionSorter);
    	return contours2;
    }
    
    public static Comparator<MatOfPoint> positionSorter = new Comparator<MatOfPoint>() {
        @Override
        public int compare(MatOfPoint c1, MatOfPoint c2) {
        	Rect r1 = Imgproc.boundingRect(c1);
        	Rect r2 = Imgproc.boundingRect(c2);

        	int n;
        	int diff = r1.y - r2.y;
        	
        	// IF y values are too close to each other (probably at the same level) then compare x values instead
            if ( diff >= -TEXTBOX_OFFSET && diff <= TEXTBOX_OFFSET )		
            	n = Double.compare(r1.x, r2.x);
            else
            	n = Double.compare(r1.y, r2.y);
            
            return n;
        }
    };
    
    // Contour Sorting
    
    public List<MatOfPoint> contourAreas(List<MatOfPoint> contours, boolean isAsc) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	if(isAsc)	Collections.sort(contours2, areaSorterAsc);
    	else		Collections.sort(contours2, areaSorterDesc);
    	return contours2;
    }
    
    public static Comparator<MatOfPoint> areaSorterDesc = new Comparator<MatOfPoint>() {
        @Override
        public int compare(MatOfPoint c1, MatOfPoint c2) {
        	int area1 = (int) Imgproc.contourArea(c1);
            int area2 = (int) Imgproc.contourArea(c2);
            return area2-area1;
        }
    };
    
    public static Comparator<MatOfPoint> areaSorterAsc = new Comparator<MatOfPoint>() {
        @Override
        public int compare(MatOfPoint c1, MatOfPoint c2) {
        	int area1 = (int) Imgproc.contourArea(c1);
            int area2 = (int) Imgproc.contourArea(c2);
            return area2+area1;
        }
    };
    
}
