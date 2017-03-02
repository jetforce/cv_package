package segmentation;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class Segmentation {

	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static FileSave fs = FileSave.getInstance();
	private static TextSegmentation textSeg = TextSegmentation.getInstance();
	
	// Field Type Variables
	private final int FIELDTYPE_TEXT = 1;
	private final int FIELDTYPE_MARK = 2;
	private final int FIELDTYPE_BLOB = 3;
	
	// Image Modification Variables
	public final int BORDER_THICKNESS_PAPER = 10;
    
    // Image
//    Mat binaryImage;
	
    private static Segmentation segmenter = new Segmentation();
    public static Segmentation getInstance() { return segmenter; }
    private Segmentation() { }
	
    public void segment(Form form) {
    	List<MatOfPoint> groupContours;
    	Mat paperImage = form.getImage();
    	
    	// PREPROCESS
		paperImage = cropBorder(paperImage, BORDER_THICKNESS_PAPER);
		cv.preprocess(paperImage);
		Imgcodecs.imwrite("test.png", paperImage);
		
		groupContours = cv.findContours(paperImage.clone(), Imgproc.RETR_EXTERNAL);
		groupContours = filterGroups(groupContours, form.getGroupCount());
		
		int[] groupTypes = form.getGroupTypes();
		
		System.out.println("[OK] SEGMENTATION: Major Groups Good");

		Mat sampleImage = paperImage.clone();
		cv.invert(sampleImage);

		List<Mat> groupImages = filter.borderRemoval(groupContours, sampleImage.clone(), true);
		
		int size = groupImages.size();
				
		for(int i = 0; i < size; i++) {
			
			switch(groupTypes[i]) {
				case FIELDTYPE_TEXT: 
					textSeg.segment(groupImages.get(i).clone(), form, i);
					System.out.println("     [OK] Group # " + i + " SEGMENTATION: Letters Good");
					break;
				case FIELDTYPE_MARK: 
					break;
				case FIELDTYPE_BLOB:
			}
		}
	}
    
    public List<MatOfPoint> filterGroups(List<MatOfPoint> contours, int elementCount) {
    	List<MatOfPoint> contours2 = new ArrayList<>(contours);
    	contours2 = filter.getLargestN(contours2, elementCount);
    	contours2 = sort.contourPositions(contours2);
    	return contours2;
    }
            
    public Mat cropBorder(Mat image, int thickness) {
        int rowStart 	= thickness;
        int rowEnd 		= image.rows() - thickness;
        int colStart 	= thickness;
        int colEnd 		= image.cols() - thickness;
        
        return image.submat(rowStart, rowEnd, colStart, colEnd);
    }
}
