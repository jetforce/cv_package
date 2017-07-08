package cv_package.testgen;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import cv_package.basicelem2.Form;
import cv_package.basicelem2.Mark;
import cv_package.basicelem2.Set;
import cv_package.basicelem2.Text;
import cv_package.basicelem2.Type;
import cv_package.debug.LocalPrinter;
import cv_package.dumps.Error;
import cv_package.dumps.Folder;
import cv_package.dumps.Images;
import cv_package.dumps.Time;
import cv_package.basicelem2.Blob;
import cv_package.filereader.FormFileReader2;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.Sorting;

public class Segmenter3 {

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static OCR3 ocr = OCR3.getInstance();
	private static OMR omr = OMR.getInstance();
	private static Folder folder = Folder.getInstance();
	private static Time time = Time.getInstance();
	private CharacterClassifier classifier;
	
	Form form;
	
	private String filepath, structpath;
	
	private Mat img;
	
	public Segmenter3() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
//	public void init(String filepath, String structpath) throws IOException {
//		this.filepath = filepath.replace("\\", "/");
//		this.structpath = structpath.replace("\\", "/");
//		
//		folder.init();
//		time.start();
//		hdc.init();
//		time.stamp("digit model done loading");
//	}
	
	public void init(String filepath, boolean isSaving, LocalPrinter printer, CharacterClassifier classifier) throws IOException {
		this.filepath = filepath.replace("\\", "/");

		folder.setSaving(isSaving);
		folder.init();
//		hdc.init();
		time.setPrinter(printer);
		time.start();
		time.stamp("digit model done loading");
		
		this.classifier = classifier;
		
		img = Imgcodecs.imread(filepath);
	}
	
	public void init(Mat image, boolean isSaving, LocalPrinter printer, CharacterClassifier classifier) throws IOException {
//		this.filepath = filepath.replace("\\", "/");

		folder.setSaving(isSaving);
		folder.init();
//		hdc.init();
		time.setPrinter(printer);
		time.start();
		time.stamp("digit model done loading");

		this.classifier = classifier;
		
		img = image;
	}
	
//	public void init(String filepath, boolean isSaving, LocalPrinter printer) throws IOException {
//		this.filepath = filepath.replace("\\", "/");
//
//		folder.setSaving(isSaving);
//		folder.init();
////		hdc.init();
//		time.setPrinter(printer);
//		time.start();
//		time.stamp("digit model done loading");
//		
//		img = Imgcodecs.imread(filepath);
//	}
//	
//	public void init(Mat image, boolean isSaving, LocalPrinter printer) throws IOException {
////		this.filepath = filepath.replace("\\", "/");
//
//		folder.setSaving(isSaving);
//		folder.init();
////		hdc.init();
//		time.setPrinter(printer);
//		time.start();
//		time.stamp("digit model done loading");
//		
//		img = image;
//	}
//	
	public void initForm() {		
		FormFileReader2 reader = new FormFileReader2();
		form = new Form();
		reader.readToForm(structpath, form);
	}
	
	public void segment() throws IOException {
		folder.save(img, "ORIG");
		
    	cv.preprocess(img);
    	folder.save(img, "PREPROC");
		
    	Mat formNumMat = getFormNumberMat(img);
    	int formNum = classifier.classifyDigit(formNumMat);
    	
    	folder.save(formNumMat, "formnummat");
    	folder.save(img, "image after get formnum");
    	
		time.stamp("form number extracting..");
//		int formNumber = getFormNumber(getbetter);
//		structpath = folder.getStructPath(formNumber);
		structpath = folder.getStructPath(1);
		initForm();
//		form.formNumber = formNumber;
		time.stamp("form init done..");
		
		form.image = img;
		go(form.image, form.components);

		time.end();
	}
	
	private Mat getFormNumberMat(Mat image) {
		List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
		
		Sorting sort = Sorting.getInstance();
		contours = sort.contourPositions(contours);
		
		MatOfPoint formNumCont = contours.get(0);
		
		Rect rect = Imgproc.boundingRect(formNumCont);
		
		Mat formNumMat = null;
		formNumMat = image.submat(rect);
		cv.invert(formNumMat);
		formNumMat = filter.removeOutlineChar(formNumMat);
		
		int start = rect.y + rect.height + 10;
		time.stamp("start: "+start);
		time.stamp("rows: "+image.rows());
		time.stamp("cols: "+image.cols());
		img = image.submat(start, image.rows(), 0, image.cols());

		return formNumMat;
	}
	
	private void go(Mat image, ArrayList<Type> components) throws IOException {
		
		List<Mat> mats = filter.largeAreaElements
				(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), components.size());
		
		int index = 0;
		
		for(Type c:components) {
			
			c.image = mats.get(index);
//			String path2 = save("3-PROCESS-"+index, c.image);
//			pb.addImage("PREPROC "+index+"-"+c.label, path2);
			folder.save(c.image, "3-PROCESS-"+index);
			
			switch(c.typename) {
			case "TEXT": 
//				long num = ocr.goo((Text)c); 
//				String num = ocr.gooString((Text)c);
//				time.stamp("num: "+num);
				ArrayList<String> chars = ocr.go((Text)c, classifier);
				time.stamp("string: "+Arrays.toString(chars.toArray()));
				break;
			case "MARK": omr.go((Mark)c); break;
			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
			default: System.out.println("Error typename invalid");
			}
			
			index++;
			
		}
	}
	
	public void goBlob(Blob comp, Mat img) {
    	img = filter.removeOutline2(img); 
		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		comp.image = img;
    }
	
}
