package cv_package.segmentation;

//import com.virtusio.sibayan.test.ComputerVision;

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

import cv_package.debug.LocalPrinter;
import cv_package.debug.LocalSaver;
import cv_package.debug.Timer;
import cv_package.fields.Text;
import cv_package.filesaving.FileSave;
import cv_package.forms.BlobAnswer;
import cv_package.forms.Form;
import cv_package.forms.MarkAnswer;
import cv_package.forms.TextAnswer;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.Sorting;
import cv_package.paperextractorv2.FourCornerBoxv2;


public class Segmentation {


	public static String TAG = "SEGMENTATION";
	private static ComputerVision cv = ComputerVision.getInstance();
	private static Sorting sort = Sorting.getInstance();
	private static Filtering filter = Filtering.getInstance();
	private static FileSave fs = FileSave.getInstance();
	private static TextSegmentation textSeg;
	private static OpticalMarkRecognition markSeg;

	private LocalSaver saver;
	private LocalPrinter printer;


	// Field Type Variables
	private final int FIELDTYPE_TEXT = 1;
	private final int FIELDTYPE_MARK = 2;
	private final int FIELDTYPE_BLOB = 3;

	// Image Modification Variables
	public final int BORDER_THICKNESS_PAPER = 10;

	// Image
//    Mat binaryImage;


	public Segmentation(LocalSaver saver,LocalPrinter printer ) {
		this.saver = saver;
		this.printer = printer;
	}

	public void segment(Form form) {

		//Extract paper here;
		FourCornerBoxv2 extract = new FourCornerBoxv2(this.saver,this.printer);
		form.setImage(extract.extractPaper(form.getImage()));

		textSeg = new TextSegmentation(saver);
		markSeg = new OpticalMarkRecognition(saver);
		
		//Timer t = Timer.getInstance();
		//t.start();
		
		printer.print("HANNAH > ", "1");

		List<MatOfPoint> groupContours;
		Mat paperImage = form.getImage();
		printer.print("HANNAH > ", "2");

		
		// PREPROCESS
		//paperImage = cropBorder(paperImage, BORDER_THICKNESS_PAPER);
		cv.preprocess(paperImage);
		//Imgcodecs.imwrite("test.png", paperImage);
		//t.stop();
		//t.start();
		printer.print("HANNAH > ", "3");

		groupContours = cv.findContours(paperImage.clone(), Imgproc.RETR_EXTERNAL);
		groupContours = filterGroups(groupContours, form.getGroupCount());

		printer.print("HANNAH > ", "4");

		int[] groupTypes = form.getGroupTypes();

		printer.print("HANNAH > ", "Ok major segments done");

		Mat sampleImage = paperImage.clone();
		cv.invert(sampleImage);

		List<Mat> groupImages = filter.borderRemoval(groupContours, sampleImage.clone(), true);

		int size = groupImages.size();
		int[] temp;

		for(int i = 0; i < size; i++) {
			//t.start();
			switch(groupTypes[i]) {
				case FIELDTYPE_TEXT:
					List<List<Mat>> images = textSeg.segment(groupImages.get(i).clone(), form, i);
					form.setAnswer(i, new TextAnswer(images));
					System.out.println("     [OK] Group # " + i + " SEGMENTATION: Letters Good");
					break;
				case FIELDTYPE_MARK:
					temp = markSeg.recognize(groupImages.get(i), form.getElementCount()[i], i);
					form.setAnswer(i,new MarkAnswer(temp));
					break;
				case FIELDTYPE_BLOB:
					form.setAnswer(i, new BlobAnswer(groupImages.get(i)));
			}
			//t.stop();
			printer.print("HANNAH > ", "group done "+i);
			
		}
		//return form;
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
